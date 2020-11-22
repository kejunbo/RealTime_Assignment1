package com.stiw3054;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Second {

    public void compareData2() {
        System.out.println("Issue 4 students");
        System.out.println("| No. | Matric | Name                                  |");
        System.out.println("|-----|--------|---------------------------------------|");
        String url1 = "https://github.com/STIW3054-A201/Main-Data/issues/1";
        String url2 = "https://github.com/STIW3054-A201/Main-Data/issues/4";
        String name1 = "";
        String name2 = "";
        String name3 = "";
        String matric = "";
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        List<String> matricList = new ArrayList<String>();
        List<String> nameList = new ArrayList<String>();
        Document doc = Jsoup.parse(url1);
        Document docc = Jsoup.parse(url2);
        Elements doc1 = doc.getElementsByClass("timeline-comment-header-text f5 text-normal");
        Elements docc2 = docc.getElementsByClass("timeline-comment-header-text f5 text-normal");

        for (Element nameIssue1 : doc1) {
            Elements url = nameIssue1.getElementsByTag("a");
            String data1 = url.attr("href");
            list1.add(data1);
        }
        for (Element nameIssue2 : docc2) {
            Elements url = nameIssue2.getElementsByTag("a");
            String data2 = url.attr("href");
            list2.add(data2);
        }

        for (String str2 : list2) {
            if (!list1.contains(str2)) {
                for (int i = 0; i < list1.size(); i++) {
                    Elements tempo = doc.getElementsByClass("js-timeline-item js-timeline-progressive-focus-container");

                    Elements url = doc.getElementsByTag("a");
                    String data1 = url.attr("href");
                    if (data1 == list1.get(i)) {
                        for (Element matricNum : tempo) {
                            Elements urll = matricNum.getElementsByTag("p");
                            String transfer = urll.select("p").toString();
                            Pattern pattern = Pattern.compile("(\\b2.*?)<br>");
                            Matcher m = pattern.matcher(transfer);
                            while (m.find()) {
                                matric = m.group(1);
                            }
                        }
                        for (Element sname : tempo) {
                            Elements urlll = sname.getElementsByTag("p");
                            String transfer = urlll.select("p").toString();
                            Pattern pattern = Pattern.compile("Name: (.*?)<br>|Name :(.*?)<br>|:\\\\s(U.*)<br>|Name:(.*?)<br>|name :( .*?)<br> |Name (.*?)<br>");// 匹配的模式 //"<p>(.*?)<br>" //:(.*)<br> //"Name: (.*?)<br>|Name :(.*?)<br>|:\\s(U.*)<br>|Name:(.*?)<br>|name :( .*?)<br> |Name (.*?)<br>"
                            Matcher m = pattern.matcher(transfer);
                            while (m.find()) {
                                name3 = m.group(0);
                            }
                        }
                        System.out.printf("|%-5s|%-8s|%-39s|%-40s|\n", i + 1, matric, name3);
                    }
                }
            }
        }
        System.out.println("|-----|--------|---------------------------------------|");
    }
}
