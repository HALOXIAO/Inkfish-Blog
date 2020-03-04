package com.inkfish.blog.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HALOXIAO
 **/
@SpringBootTest
public class test {

    private void print(Analyzer analyzer) throws Exception {
        String text = "Hello World This is Halo";
        TokenStream tokenStream = analyzer.tokenStream("content", text);
        CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            System.out.println(new String(attribute.toString()));
        }
    }

    @Test
    public void contextLoads()throws Exception{
    }
}
