import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class WordCounter{
    HashMap<String,Integer> map=new HashMap<>();
    public synchronized void CountAll(String s){
        Scanner scanner=new Scanner(s);
        scanner.useDelimiter("[\\s,\\.]+");
        while (scanner.hasNext()){
            String word=scanner.next().toLowerCase();
            map.merge(word, 1, Integer::sum);
        }
    }
    public void display(){
        map.entrySet().stream().map(set -> set.getKey() + " = "
                + set.getValue()).forEach(System.out::println);

    }
}
class FileProcessor extends Thread{
    WordCounter counter;
    int paragraph;
    String file;
    String content;
    public FileProcessor(String file,WordCounter count,int para)
    {
        this.file=file;
        paragraph=para;
        counter=count;
    }
    @Override
    public void run()
    {
        File obj=new File(file);

        try {
            Scanner scanner=new Scanner(obj);
            int i=0;
                while(scanner.hasNext() && i<paragraph){
                    if(scanner.nextLine().isEmpty())
                        i++;
                }
            content=scanner.nextLine();
            counter.CountAll(content);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
public class Multithreading {
    public static void main(String[] args) {
        WordCounter counter = new WordCounter();
        int number_of_paragraphs=5;
        List<FileProcessor> threads;
        threads = new ArrayList<>();

        for (int i = 0; i < number_of_paragraphs; i++) {
            FileProcessor fp = new FileProcessor("C:\\Users\\Dell\\IdeaProjects\\Multithreading\\src\\sample.txt", counter, i);
            threads.add(fp);
            fp.start();
        }
            //  you would have to provide the absolute path of the file
        // Wait for all threads to finish
        for (FileProcessor thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Now, all threads have finished, and you can safely call display
        counter.display();
    }
}