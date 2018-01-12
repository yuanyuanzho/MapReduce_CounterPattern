package com.assignment.hw4_Part2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.counters.Limits;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class Driver 
{
    public static void main( String[] args ) throws Exception
    {
    	Configuration conf = new Configuration();
    	conf.setInt(MRJobConfig.COUNTERS_MAX_KEY, 20000);
    	Limits.init(conf);
    	
//		String[] otherArgs = new GenericOptionsParser(conf, args)
//				.getRemainingArgs();

		if (args.length != 2) {
			System.err.println("Usage: CountNumUsersByState <users> <out>");
			System.exit(2);
		}
		
		

		Path input = new Path(args[0]);
		Path outputDir = new Path(args[1]);

		Job job = new Job(conf, "Count Num Users By State");
		job.setJarByClass(CountIPMapper.class);

		job.setMapperClass(CountIPMapper.class);
		job.setNumReduceTasks(0);

		
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(NullWritable.class);

		FileInputFormat.addInputPath(job, input);
		FileOutputFormat.setOutputPath(job, outputDir);
		

		
		int code = job.waitForCompletion(true) ? 0 : 1;

		if (code == 0) {
			for (Counter counter : job.getCounters().getGroup(
					CountIPMapper.IP_COUNTER_GROUP)) {
				System.out.println(counter.getDisplayName() + "\t"
						+ counter.getValue());
			}
		}

		
		
		FileSystem.get(conf).delete(outputDir, true);

		System.exit(code);
    }
}
