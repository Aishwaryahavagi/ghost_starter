package com.ap.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary
{
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null)
        {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(word);
        }
    }

    @Override
    public boolean isWord(String word)
    {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix)
    {
        if(prefix == null || prefix == "")
        {
            Random random = new Random();
            String randomWord = words.get(random.nextInt(words.size()));
            return  randomWord;
        }
        else
        {
            int low = 0, high = words.size() - 1;
            int mid;
            String t;

            while(low < high)
            {
                mid = (low + high) / 2;
                t = words.get(mid);
                if (t.startsWith(prefix))
                {
                    return t;
                }
                else if (prefix.compareTo(t) > 0)
                {
                    // LHS is bigger
                    low = mid + 1;
                }
                else
                {
                    // RHS is bigger
                    high = mid - 1;
                }
            }
            return null;
        }

    }

    @Override
    public String getGoodWordStartingWith(String prefix)
    {
        String selected = null;
        Random random = new Random();
        if(prefix == "")//if computer starts return any random word
        {
            return words.get(random.nextInt(words.size()));
        }
        ArrayList<String> oddWordSet = new ArrayList<>();
        ArrayList<String> evenWordSet = new ArrayList<>();
        int rangeBegin=-1,rangeEnd=-1;//all words starting with prefix
        int begin = 0, end = words.size()-1,mid;
        while (begin<=end)
        {
            Log.d("Begin"+begin,"End"+end);
            mid=(begin+end)/2;
            Log.d("Mid"+mid,"Word"+words.get(mid));
            if(words.get(mid).startsWith(prefix))
            {
                Log.d("TRue","Prefix"+prefix);
                for(int i=mid-1;i>=0;i--)
                {
                    if(!words.get(i).startsWith(prefix))
                    {
                        rangeBegin=i+1;
                        break;
                    }
                }
                for(int i=mid+1;i<words.size();i++)
                {
                    if(!words.get(i).startsWith(prefix))
                    {
                        rangeEnd=i-1;
                        break;
                    }
                }
                break;
            }
            else
            {
                if(words.get(mid).compareTo(prefix)>0)
                {
                    end = mid-1;
                }
                else
                {
                    begin = mid +1;
                }
            }
        }
        if(rangeBegin == -1 || rangeEnd == -1)
            return selected;//null;
        for(int i=rangeBegin;i<rangeEnd;i++)
        {
            if(words.get(i).length()%2==0)
                evenWordSet.add(words.get(i));
            else
                oddWordSet.add(words.get(i));
        }
        Log.d("even",""+evenWordSet);
        Log.d("odd",""+oddWordSet);
        if(prefix.length()%2==0)//odd prefix odd word
        {
            if(!oddWordSet.isEmpty())
                selected=oddWordSet.get(random.nextInt(oddWordSet.size())); //Random word better than largest word to avoid repeated words
            else {
                if (!evenWordSet.isEmpty())
                    selected = evenWordSet.get(random.nextInt(evenWordSet.size())); //when computer knows that it has to loose
                //else no words
            }
        }
        else//even prefix even word
        {
            if(!evenWordSet.isEmpty())
                selected=evenWordSet.get(random.nextInt(evenWordSet.size()));
            else{
                if (!oddWordSet.isEmpty())
                    selected=oddWordSet.get(random.nextInt(oddWordSet.size()));
            }
        }

        return selected;
    }

}
