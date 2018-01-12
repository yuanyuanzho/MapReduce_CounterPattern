package com.assignment.hw4_Part2;

import java.io.IOException;
import java.util.HashSet;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.counters.LimitExceededException;

public class CountIPMapper extends Mapper<Object, Text, NullWritable, NullWritable> {

	public static final String IP_COUNTER_GROUP = "IP_Counter";
	
	
	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException{
		
		String[] fields = value.toString().split("- -");
		String ip = fields[0];
		
		context.getCounter(IP_COUNTER_GROUP, ip).increment(1); 
		
		
		
	}
}
