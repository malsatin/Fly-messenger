package com.example.denis.p7.algorithms.coding;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.denis.p7.algorithms.exceptions.DecodingException;
import com.example.denis.p7.algorithms.interfaces.ICoder;

import java.util.BitSet;

public class RepetitionCode implements ICoder {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public byte[] encodeByteString(byte[] message) {
        BitSet set = BitSet.valueOf(message);
        BitSet answer = new BitSet();
        int index = 0;
        for (int i = 0; i < set.length(); i++) {
            boolean temp = set.get(i);
            for (int k = 0; k < 3; k++) {
                answer.set(index++, temp);
            }
        }
        return answer.toByteArray();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public byte[] decodeByteString(byte[] sequence) throws DecodingException {
        int index = 0;
        BitSet set = BitSet.valueOf(sequence);
        BitSet answer = new BitSet();
        for (int i = 0; i <sequence.length;i++){
            if(i%3==0){
                if (answer.get(i)&answer.get(i+1)|answer.get(i+1)&answer.get(i+2)) answer.set(index++,true);
                else set.set(index++,false);
            }
        }
        return answer.toByteArray();
    }
}
