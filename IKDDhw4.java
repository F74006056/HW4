/*HW4 ²Ä¤»²Õ ¼B¥°°¶ ¶À¬f·ì
 * input:command line OR Scanner
 * output:a table contains rank,filename by pagerank
 * 
 * environment:eclipse in windows
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class IKDDhw4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String instr;
		if(args.length==0)
		{
			Scanner sc=new Scanner(System.in);
			System.out.println("input query string:");
			instr=sc.nextLine();
			sc.close();
		}
		else
		{
			instr=args[0];
		}
		String path ="webpage_data_5";
		File dir=new File(path);
		int filenum=dir.list().length;
		int deadnum=filenum;
		int yesnum=0;
		boolean yes[]=new boolean[filenum];
		boolean dead[]=new boolean[filenum];
		double matric[][]=new double[filenum][filenum];
		double result[]=new double[filenum];
		if(dir.isDirectory())
		{
			for(int i1=0;i1<filenum;++i1)
			{
				try
				{
					BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(path+"\\"+dir.list()[i1]),"Big5"));
					String ts;
					String total="";
					while((ts=in.readLine())!=null)
					{
						total=total+ts;
					}
					total=total.replaceAll("\n","");
					total=total.replaceAll("\r","");
					if(total.contains(instr))
					{
						yes[i1]=true;
						yesnum++;
					}					
					int count=0;
					for(int i2=0;i2<filenum;++i2)
					{
						if(total.contains("http://"+dir.list()[i2]))
						{
							count++;
							matric[i2][i1]=1;
						}
						else
						{
							matric[i2][i1]=0;
						}
					}
					if(count!=0)
					{
						for(int i2=0;i2<filenum;++i2)
						{
							matric[i2][i1]/=count;
						}
					}
					in.close();
					dead[i1]=true;
				}
				catch(Exception e)
				{
					
				}
			}
			boolean wloop=true;
			while(wloop)
			{
				wloop=false;
				for(int i=0;i<filenum;++i)
				{
					if(dead[i])
					{
						boolean check=true;
						for(int i1=0;i1<filenum;++i1)
						{
							if(dead[i1])
							{
								if(matric[i1][i]!=0)
								{
									check=false;
									break;
								}
							}
						}
						if(check)
						{
							dead[i]=false;
							deadnum--;
							wloop=true;
						}
					}
				}
			}

			double smu[][]=new double[deadnum][deadnum];
			double sre[]=new double[deadnum];
			for(int i=0,j=0;i<filenum;++i)
			{
				if(dead[i])
				{
					int count=0;
					for(int i1=0,j1=0;i1<filenum;++i1)
					{
						if(dead[i1])
						{
							if(matric[i1][i]!=0)
							{
								smu[j1][j]=1;
								count++;
							}
							else
								smu[j1][j]=0;
							j1++;
						}
					}
					for(int i1=0;i1<deadnum;++i1)
					{
						smu[i1][j]/=count;
					}
					sre[j]=1.0/deadnum;
					j++;
				}
			}
			for(int i=0;i<100;++i)
			{
				double temres[]=new double[deadnum];
				for(int i1=0;i1<deadnum;++i1)
				{
					temres[i1]=0;
					for(int i2=0;i2<deadnum;++i2)
					{
						temres[i1]+=(smu[i1][i2]*sre[i2]);
					}
				}
				int count=0;
				for(int i1=0;i1<deadnum;++i1)
				{
					if(temres[i1]==sre[i1])
						count++;
					else
						sre[i1]=temres[i1];
				}
				if(count==deadnum)
					break;
			}
			for(int i=0,j=0;i<filenum;++i)
			{
				if(dead[i])
				{
					result[i]=sre[j];
					j++;
				}
			}
			while(deadnum!=filenum)
			{
				for(int i=0;i<filenum;++i)
				{
					if(!dead[i])
					{
						result[i]=0;
						boolean check=true;
						for(int i1=0;i1<filenum;++i1)
						{
							if(matric[i][i1]!=0)
							{
								if(i!=i1&&dead[i1])
									result[i]+=(matric[i][i1]*result[i1]);
								else if(i!=i1&&!dead[i1])
								{
									check=false;
									break;
								}
							}
						}
						if(check)
						{
							deadnum++;
							dead[i]=true;
						}
					}
				}
			}
			System.out.println("Rank\t\tFilename");
			for(int i=0;i<yesnum;++i)
			{
				System.out.print((i+1)+"\t\t");
				double max=0;
				int loca=0;
				for(int i1=0;i1<filenum;++i1)
				{
					if(yes[i1]&&max<result[i1])
					{
						max=result[i1];
						loca=i1;
					}
				}
				yes[loca]=false;
				System.out.println(dir.list()[loca]);
			}
		}
	}

}
