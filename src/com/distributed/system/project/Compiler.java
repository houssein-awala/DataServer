package com.distributed.system.project;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

/*
*AUTHOR: HUSSEIN AWALA
*this class is a compiler for a Request object
* devides the request to simples numbers and variables
* then generate the response as String variable
 */

public class Compiler{
    private String[] types;
    private int[] numbers;
    Random random;

    //this method devides the request into two array
    public Compiler(String request) {
        random=new Random();
        StringTokenizer tokenizer=new StringTokenizer(request);
        types=new String[tokenizer.countTokens()];
        numbers=new int[tokenizer.countTokens()];
        int i=0;
        while (tokenizer.hasMoreTokens())
        {
            String[] temp=tokenizer.nextToken().split(":");
            numbers[i]=Integer.parseInt(temp[0]);
            types[i++]=temp[1];
        }
    }

    //this method take a type as a string and return a variable of this type
    public Object variable(String type)
    {

        switch (type)
        {
            case "INTEGER":return random.nextInt(256);
            case "FLOAT":return random.nextFloat()*256;
        }
        return null;
    }

    //this method generate the response using the variable method
    public String compile()
    {
        String res="";
        for (int i=0;i<numbers.length;i++)
        {
            for (int j=0;j<numbers[i];j++)
            {
                if (!res.equals(""))
                    res+=" ";
                res+=variable(types[i]);
            }
        }
        return res;
    }
}
