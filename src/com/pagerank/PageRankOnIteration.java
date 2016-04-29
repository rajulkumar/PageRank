package com.pagerank;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.crypto.NodeSetData;

public class PageRankOnIteration{

	static HashMap<String, List<Double>> pageRank=new HashMap<String, List<Double>>();
	static HashMap<String, Integer>  nodeWithOutLinks=new HashMap<String, Integer>();
	static HashMap<String, String[]> nodeWithInLinks= new HashMap<String, String[]>();
	
	
	static List<Double> perplexity= new ArrayList<Double>();
	static Set<String> sinkNodes = new HashSet<String>();
	
	static double teleportFactor=0.85;
	static double initialPR=0.0;
	static int evaluationType=-1;
	
	
	static String FILE_FOR_PERPLEXITY="output/perplexity.txt";
	static String FILE_FOR_PAGE_RANK="output/pagerank.txt";
	static String FILE_FOR_PAGE_WITH_IN_LINKS="output/inlink.txt";
	static String FILE_FOR_PROPORTION="output/proportions.txt";
	static String FILE_FOR_EACH_RANK="output/ranks.txt";
	
	static String FILE_AFTER_1="output/rank_after_1.txt";
	static String FILE_AFTER_10="output/rank_after_10.txt";
	static String FILE_AFTER_100="output/rank_after_100.txt";
	
	public static void parseData(String fileName) throws Exception
	{
		FileInputStream fs=null;
		BufferedReader br=null;
		try
		{
			fs=new FileInputStream(new File(fileName));
			
		br=new BufferedReader(new InputStreamReader(fs));
		int i=0;
		String line=null;
		String[] links=null;
		
		//System.out.println("initsize::"+nodeWithInLinks.size());
		while(null!=(line=br.readLine()))
		{
			i++;
			links=line.split(" ");
			nodeWithInLinks.put(links[0], links); // includes the node at first pos
		
			pageRank.put(links[0],null);
			setNodeWithOutLinks(links);
			
		}
		///System.out.println("Lines read:::"+i);
		}
		finally{
			br.close();
			fs.close();
		}
	}
	
	private static void setNodeWithOutLinks(String[] links)
	{
		Set<String> tempNodes=new HashSet<String>();
		if(!nodeWithOutLinks.containsKey(links[0]))
		{
			nodeWithOutLinks.put(links[0], 0);
		}
		
		String node=null;
		
		for(int i=1;i<links.length;i++)
		{
			node=links[i].trim();
			//System.out.println("node:"+node);
			if(!tempNodes.contains(node))
			{
				tempNodes.add(node);
				if(nodeWithOutLinks.containsKey(node))
				{
					//System.out.println("replace:"+node+"::"+nodeWithOutLinks.get(node));
					nodeWithOutLinks.put(node,nodeWithOutLinks.get(node)+1);
				}
				else
				{
					//System.out.println("add:"+node);
					nodeWithOutLinks.put(node, 1);
				}
			}
			
			if(!pageRank.containsKey(node))
			{
				pageRank.put(node, null);
			}
		}
	}
	
	public static void main(String arg[]) throws Exception
	{
		long start=System.nanoTime();
		
		parseData(arg[0]);
		setSinkNodes();
		setInitialPR();
		//*********************************
		//writeOutLinks();
		//*************************
		processNextPageRank();
		
		long end=System.nanoTime();

	}
	

	
	private static void propData() throws Exception
	{
		int sources=0;
		int pagesWithRankLessThanInitial=0;
		String[] inLinks=null;
		for(Entry<String,List<Double>> node:pageRank.entrySet())
		{
			if(null!=(inLinks=nodeWithInLinks.get(node.getKey())))
			{		
				if(inLinks.length<=1)
					sources++;
			}
			else
			{
				sources++;
			}
			
			if(node.getValue().get(1)<initialPR)
			{
				pagesWithRankLessThanInitial++;
			}
		}
		System.out.println("sources::"+sources);
		
			
		StringBuffer buff=new StringBuffer();
		buff.append("total pages::"+pageRank.size());
		buff.append(System.getProperty("line.separator"));
		buff.append(System.getProperty("line.separator"));
		
		buff.append("pages with no in-links(sources)::"+sources);
		buff.append(System.getProperty("line.separator"));
		buff.append("proportion of pages with no in-links (sources)::"+((double)sources/pageRank.size()));
		buff.append(System.getProperty("line.separator"));
		buff.append("********************************************************************");
		buff.append(System.getProperty("line.separator"));
		
		buff.append("pages with no out-links (sinks)::"+sinkNodes.size());
		buff.append(System.getProperty("line.separator"));
		buff.append("proportion of pages with no out-links (sinks)::"+((double)sinkNodes.size()/pageRank.size()));
		buff.append(System.getProperty("line.separator"));
		buff.append("********************************************************************");
		buff.append(System.getProperty("line.separator"));
		
		buff.append("pages whose PageRank is less than their initial, uniform values::"+pagesWithRankLessThanInitial);
		buff.append(System.getProperty("line.separator"));
		buff.append(" proportion of pages whose PageRank is less than their initial, uniform values::"
		+((double)pagesWithRankLessThanInitial/pageRank.size()));
		buff.append(System.getProperty("line.separator"));
		buff.append("********************************************************************");
		buff.append(System.getProperty("line.separator"));
		
		writeToFile(buff, FILE_FOR_PROPORTION);

	}
	
	private static void listOfPagesByPageRank() throws Exception
	{
		StringBuffer buffer=new StringBuffer();
		List<Map.Entry<String, List<Double>>> list = 
				new ArrayList<Map.Entry<String, List<Double>>>(pageRank.entrySet());

			// Sort list with comparator, to compare the Map values
			Collections.sort(list, new Comparator<Map.Entry<String, List<Double>>>() {
				public int compare(Map.Entry<String, List<Double>> o1,
	                                           Map.Entry<String, List<Double>> o2) {
					return(o1.getValue().get(1)).compareTo(o2.getValue().get(1));
				}
			});
			
			//System.out.println("listsize::"+list.size());
			//System.out.println("val::"+list.get(1).getKey()+" :: "+list.get(1).getValue().get(1));
			//Object[] rank=pageRank.entrySet().toArray();
				for(int i=list.size()-1;i>(list.size()-51);i--)
				{
					//System.out.println("written::"+i);
					buffer.append(list.get(i).getKey()+" :: "+list.get(i).getValue().get(1));
					buffer.append(System.getProperty("line.separator"));
				}
				
				writeToFile(buffer, FILE_FOR_PAGE_RANK);
	}
	
	private static void listOfPagesByInLink() throws Exception
	{
		StringBuffer buffer=new StringBuffer();
		List<Map.Entry<String, String[]>> list = 
				new ArrayList<Map.Entry<String, String[]>>(nodeWithInLinks.entrySet());

			// Sort list with comparator, to compare the Map values
			Collections.sort(list, new Comparator<Map.Entry<String, String[]>>() {
				public int compare(Map.Entry<String, String[]> o1,
	                                           Map.Entry<String, String[]> o2) {
					return((Integer)o1.getValue().length).compareTo(o2.getValue().length);
				}
			});
			
			
			for(int i=list.size()-1;i>(list.size()-51);i--)
				{
			
					buffer.append(list.get(i).getKey()+" :: "+(list.get(i).getValue().length-1));
					buffer.append(System.getProperty("line.separator"));
				}
				
				writeToFile(buffer, FILE_FOR_PAGE_WITH_IN_LINKS);
	}
		
	
	
	private static void writeOutLinks() throws Exception
	{
		StringBuffer buff=new StringBuffer();
		for(Entry<String,Integer> link:nodeWithOutLinks.entrySet())
			{
				buff.append(link.getKey()+"::"+link.getValue()+":");
				buff.append(System.getProperty("line.separator"));
			}
		writeToFile(buff, "src/com/pagerank/outlinks.txt");
	}
	
	private static void writePageRank(int itr,String fileName) throws Exception
	{
		StringBuffer buffer=new StringBuffer();
		double prSum=0;
		
		
			for(Entry<String,List<Double>> rank:pageRank.entrySet())
			{
				//System.out.println(rank.getKey()+" :: "+rank.getValue().get(1));
				buffer.append(rank.getKey()+" :: "+rank.getValue().get(1));
				buffer.append(System.getProperty("line.separator"));
			}
		
			
			
		writeToFile(buffer, fileName);
	}
	
	private static void listOfPerplexityValues() throws Exception
	{
		StringBuffer buffer=new StringBuffer();
		
		for(Double perp: perplexity)
		{
			buffer.append(perp);
			buffer.append(System.getProperty("line.separator"));
		}
		writeToFile(buffer, FILE_FOR_PERPLEXITY);
	}
	
	private static void writeToFile(StringBuffer data, String fileName) throws Exception
	{
		FileOutputStream fo=null;
		BufferedWriter bw=null;
		
		try
		{
			
			bw=new BufferedWriter(new FileWriter(new File(fileName)));
			bw.write(data.toString());
		}
		finally
		{
			bw.flush();
			bw.close();
		}
		
	}
	
	private static void appendToFile(StringBuffer data, String fileName) throws Exception
	{
		FileOutputStream fo=null;
		BufferedWriter bw=null;
		
		try
		{
			
			bw=new BufferedWriter(new FileWriter(new File(fileName),true));
			bw.write(data.toString());
		}
		finally
		{
			bw.flush();
			bw.close();
		}
		
	}
	
	
	private static double calculatePerplexity()
	{
		double entropy=0;
		double pgRank=0;
		for(Entry<String,List<Double>> rank:pageRank.entrySet())
		{
			pgRank=rank.getValue().get(1);
			entropy+=pgRank*(Math.log(pgRank)/Math.log(2));
			
		}
		
		double perplexity=Math.pow(2, -entropy);
		//System.out.println("Perplexity::"+perplexity+"::"+ -entropy);
		return perplexity;
	}
	
	public static void processNextPageRank() throws Exception
	{
		int counter=0;
		boolean check=true;
		double sinkPR=0d;
		String parentPage="";
		
		int noOfPages=pageRank.size();
		double newPR=0;
		
		while(check)
		{
			shiftPageRanks();
			//counter++;
			sinkPR=calculateSinkPR();
			
			for(Entry<String,List<Double>> page:pageRank.entrySet())
			{
				newPR=0;
				newPR=(1-teleportFactor)/noOfPages;
				
				newPR=newPR+(teleportFactor*(sinkPR/noOfPages));
				Set<String> parentSet=new HashSet<String>();
				if(null!=nodeWithInLinks.get(page.getKey())){
					String[] inLink=nodeWithInLinks.get(page.getKey());
				for(int itr=1;itr<inLink.length;itr++)
				{
					parentPage=inLink[itr];
					//System.out.println("parentpage::"+parentPage);
					if(!parentSet.contains(parentPage))
					{
						parentSet.add(parentPage);
					if(null!=pageRank.get(parentPage))
					{
						newPR=newPR+(teleportFactor*(pageRank.get(parentPage).get(0)/nodeWithOutLinks.get(parentPage)));
					}
					}
				}
				}
				List updatedPR=new ArrayList<Double>();
				//System.out.println("pr::"+inLink.getKey()+"::"+pageRank.get(inLink.getKey()));
				updatedPR.add(pageRank.get(page.getKey()).get(0));
				updatedPR.add(newPR);
				//System.out.println("updatedPR::"+updatedPR+"::"+newPR);
				pageRank.put(page.getKey(),updatedPR);
				
			}
			
			if(counter==0)
				writePageRank(-1, FILE_AFTER_1);
			else if(counter==9)
				writePageRank(-1, FILE_AFTER_10);
			else if(counter==99)
			{
				writePageRank(-1, FILE_AFTER_100);
				check=false;
			}
			
			counter++;
			
		}
//		System.out.println("sinkPR::"+sinkPR);
		writePageRank(counter+1,FILE_FOR_EACH_RANK);
	}
	
	private static boolean converge(int i){
		
		if(i==0)
		{
			return true;
		}
		else
		{
			return (((Math.abs(perplexity.get(perplexity.size()-i)-perplexity.get(perplexity.size()-(i+1))))<1.0)
						&& (converge (i-1)));
							
		}
	}
	
	private static void shiftPageRanks()
	{
		for(List<Double> ranks:pageRank.values())
		{
			ranks.set(0, ranks.get(1));
		}
	}
	
	private static double calculateSinkPR()
	{
		double sinkPR=0d;
		for(String sinkNode:sinkNodes)
		{
			sinkPR+=pageRank.get(sinkNode).get(0);
		}
		return sinkPR;
	}
	
	public static void setSinkNodes()
	{

		for(Entry<String,Integer> outLinks:nodeWithOutLinks.entrySet())
		{
			if(null!=outLinks.getKey() && null!=outLinks.getValue()){
			if(outLinks.getValue()==0)
			{
				sinkNodes.add(outLinks.getKey());
			}
			}
		}
	}

	public static void  setInitialPR()
	{
		initialPR=1d/pageRank.size();
		List<Double> prList=new ArrayList<Double>(2);
		prList.add(initialPR);
		prList.add(initialPR);
		Iterator<Entry<String, List<Double>>> itr=pageRank.entrySet().iterator();
		while(itr.hasNext()){
			itr.next().setValue(prList);
		}
	}
}

