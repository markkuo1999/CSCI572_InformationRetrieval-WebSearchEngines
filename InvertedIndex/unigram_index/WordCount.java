import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.util.HashMap;
import java.util.Map;


public class WordCount {
   public static class TokenizerMapper extends Mapper<Object, Text, Text, Text>
   {
      private final static IntWritable one = new IntWritable(1);
      private Text word = new Text();
      private Text document_id = new Text();


      public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
      {
         
         StringTokenizer itr = new StringTokenizer(value.toString());
         String id = itr.nextToken();
         
         document_id.set(id);
         String cleanText = value.toString().replaceAll("[^a-zA-Z\\s]", " ");
         cleanText = cleanText.toLowerCase();
         itr =  new StringTokenizer(cleanText);
         while (itr.hasMoreTokens()) {          
             word.set(itr.nextToken());
             context.write(word, document_id);      
                                                                                     
         }
         
      }

       
   }
   
   public static class IntSumReducer extends Reducer<Text,Text,Text,Text> 
   {
      
      Text result = new Text();
      public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException 
      {      
         HashMap<String, Integer> wordcountmap = new HashMap<>();        
         for (Text val : values) 
         {          
            if(wordcountmap.containsKey(val.toString()))
            {
                wordcountmap.put(val.toString(), wordcountmap.get(val.toString())+1);
            }
            else
                wordcountmap.put(val.toString(), 1);

         }
     
         StringBuilder multiwordcount = new StringBuilder();
       
         for (Map.Entry<String, Integer> entry : wordcountmap.entrySet()) {           
             String wordcount = entry.getKey() + ":" + String.valueOf(entry.getValue());
             multiwordcount.append(wordcount).append(" ");                     
         }
              
         result = new Text(multiwordcount.toString());

         context.write(key, result);
      }
   }
   
   public static void main(String[] args) throws Exception 
   {
      Configuration conf = new Configuration();
      Job job = Job.getInstance(conf, "word count");
		
      job.setJarByClass(WordCount.class);
      job.setMapperClass(TokenizerMapper.class);
      //job.setCombinerClass(IntSumReducer.class);
      job.setReducerClass(IntSumReducer.class);
		
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(Text.class);
		
      FileInputFormat.addInputPath(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
      System.exit(job.waitForCompletion(true) ? 0 : 1);
   }
}// WordCount

