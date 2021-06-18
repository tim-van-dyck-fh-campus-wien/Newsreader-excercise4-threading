package at.ac.fhcampuswien.downloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelDownloader extends Downloader {
    private ExecutorService executor = Executors.newFixedThreadPool(8);//Create new thredpool of size 8 concurrent threads

    @Override
    public int process(List<String> urls) {
        List<List<String>> subLists = splitLists(urls,2);//Split into a maximum of 8 sublists...
        /*System.out.println("NUMBER OF SUBLISTS");
        System.out.println(subLists.size());
        int idx=0;
        for(List<String> curList :subLists){
            System.out.println("LIST OF INDEX " + idx);
            for(String val :curList){
                System.out.println(val);
            }
            System.out.println();
            idx++;
        }*/
        List<DownloadThread> threads = new ArrayList<>();
        List<Future<Integer>> futures = new ArrayList<>();//List containing the futures
        for(List<String> urlList:subLists){
            futures.add(executor.submit(new DownloadThread(urlList)));//Insert into executor, and save the futures
        }
        int sum = 0;//Saves the total number of sucessful downloads
            for(Future<Integer> future:futures){
                try {
                    //.get blocks, until result is ready!
                    sum += future.get();//Add the sum of sucessful downloads of current thread
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
       // System.out.println(sum+" DOWNLOADS WERE SUCESSFUL!");
        return sum;
    }
    //Recursively split url list into sublists for the threads
    //2^depth+1 sublists created at max!
    public List<List<String>> splitLists(List<String> urls,int depth){
        if((urls.size()>1)&&depth>=0){
            int midIndex = (urls.size()-1)/2;
            List<List<String>> lists = new ArrayList<>(urls.stream()
            .collect(Collectors.partitioningBy(s-> urls.indexOf(s) > midIndex))
            .values()
            );
            //recursivly split the two sublists into further sublists!
            List<List<String>> sublist1 = splitLists(lists.get(0),depth-1);
            List<List<String>> sublist2 = splitLists(lists.get(1),depth-1);
            return Stream.concat(sublist1.stream(),sublist2.stream()).collect(Collectors.toList());
        }else{
            List<List<String>> ret = new ArrayList<>();
            ret.add(urls);
            return ret;
        }
    }
}
//Extends Downloader to be able to access the saveUrl2File method in paralell
class DownloadThread extends Downloader implements Callable<Integer> {
    List<String> urls;
    public DownloadThread(List<String> urls){
        this.urls=urls;
    }

    @Override
    public Integer call() throws Exception {
        int count = 0;
        for (String url : urls) {
            String fileName = saveUrl2File(url);
            if(fileName != null)
                count++;
        }
        return count;
    }

    @Override
    public int process(List<String> urls) {
        return 0;
    }
}
