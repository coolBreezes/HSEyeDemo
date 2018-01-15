package com.example.umeyesdk.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class LocalFile 
{
	private static List<String> files=null;
	/**
	 * ��ȡ�ļ����ڵ����ź�׺Ϊext��ʶ���ļ�
	 * @param dir Ŀ���ļ���
	 * @param ext ��׺�������׺��#�ָ
	 * @return ��ȡ�ɹ������б�ʧ�ܷ��ؿ�
	 */
	public static List<String> GetMatchFile(String dir,String ext)
	{
		List<String> list=new ArrayList<String>();
		String[] a=ext.split("#");
		File[] allFiles=null;
		File file=null;
		try
		{
			file=new File(dir);
			allFiles=file.listFiles();
			if(file.isDirectory())
			{
				for (int k = 0; k < allFiles.length; k++)
				{
					if(!allFiles[k].isDirectory())
					{
						for (int i = 0; i < a.length; i++) 
						{
							if(allFiles[k].getName().toLowerCase().endsWith(a[i].toLowerCase()))
							{
								list.add(allFiles[k].getPath());
							}
						}
					}
				}
			}
			else
			{
				throw new IllegalArgumentException();
			}
		}
		catch(Exception e)
		{
			System.out.println("��ȡ�ļ����б����"+e.getMessage());
			e.printStackTrace();
			return null;
		}
		return list;
	}
	/**
	 * �����ļ��У�������ڻ��ߴ����ɹ�����Ϊtrue,����Ϊfalse
	 * @param dir
	 * @return
	 */
	public static boolean CreateDirectory(String dir)
	{
		File file=new File(dir); 
		boolean b=false;
		Log.d("CreateDirectory", file.exists()+"�Ƿ�����ļ���"+dir);
		if(!file.exists())
		{
			b=file.mkdirs();
			Log.d("CreateDirectory", file.exists()+"�Ƿ񴴽��ɹ�"+file.mkdirs());
			System.out.println();
			System.out.println("�����ڣ������ļ���"+b+dir);
			
			return b;
		}
		else
		{
			System.out.println("�����ļ���"+dir);
			return true;
		}
	}
	public static void SaveImage(Bitmap bmp,String name,String dir)
	{
		
		OutputStream out=null;
		try
		{
			out=new FileOutputStream(dir+name);
			bmp.compress(CompressFormat.JPEG, 100, out);
			out.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(out!=null)
			{
				try
				{
					out.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * �õ�Ŀ¼dir�����к�׺��Ϊext���ļ��б�
	 * @param dir
	 * @param ext
	 */
	private static void GetFiles(String dir,String ext)
	{
		System.out.println("���ļ�~~~~~~~~~~~~~~~~~~~~~~"+dir+"�ļ�����");
		File file=new File(dir);
		File[] fs=file.listFiles();
		if(fs==null || fs.length==0) return;
		for(int i=0;i<fs.length;i++)
		{
			if(fs[i].isFile() && fs[i].getName().endsWith(ext.toLowerCase()))
			{
				files.add(fs[i].getPath());
				System.out.println("�ҵ��ļ���"+fs[i].getPath());
			}
			else if(fs[i].isDirectory())
			{
				 GetFiles(fs[i].getPath(),ext);
			}
		}
	}
	/**
	 * �õ�Ŀ¼dir�����к�׺��Ϊext���ļ��б�
	 * @param dir Ŀ¼
	 * @param ext ��׺��
	 * @return �ļ��б�
	 */
	public static List<String> GetMatchExtFiles(String dir,String ext)
	{
		if(files==null)
		{
			files=new ArrayList<String>();
		}
		else
		{
			files.clear();
		}
		GetFiles(dir,ext);
		return SortedByCreateTime(files);
	}
	
	/*public static void getModifiedTime_2(){   
        File f = new File("C:\\test.txt");               
        Calendar cal = Calendar.getInstance();   
        long time = f.lastModified();   
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");          
        cal.setTimeInMillis(time);     
        System.out.println("�޸�ʱ��[2] " + formatter.format(cal.getTime()));      
        //������޸�ʱ��[2]    2009-08-17 10:32:38   
    }   */
	
	private static List<String> SortedByCreateTime(List<String> list)
	{
		for(int i=0;i<list.size();i++)
		{
			for(int k=i+1;k<list.size();k++)
			{
				String s1=list.get(i);
				String s2=list.get(k);
				Log.w("s1","s1 is:"+s1+" s2 is:"+s2);
				
		        File f1 = new File(s1);               
		       // Calendar cal = Calendar.getInstance();   
		        long time1 = f1.lastModified();   
		       // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");          
		       // cal.setTimeInMillis(time);     
		        f1=null;
		        
		        File f2 = new File(s2);               
		       // Calendar cal2 = Calendar.getInstance();   
		        long time2 = f2.lastModified();   
		        f2=null;
		       // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");          
		       // cal.setTimeInMillis(time);  
		        
		       // System.out.println("�޸�ʱ��[2] " + formatter.format(cal.getTime())); 
		        
				//if(Compare(s1, s2))//����i���С��k�㣬�ͽ���ֵ
		        if(time1<time2)
				{
					list.set(i, s2);
					list.set(k, s1);
				}
			}
		}
		return list;
	}
	/**
	 * ���s1�Ƿ�С��s2������ǣ�����True������False
	 * @param s1
	 * @param s2
	 * @return
	 */
	/*public static boolean Compare(String s1,String s2)
	{
		String v1=s1.substring(s1.lastIndexOf('/')+1,s1.lastIndexOf('.'));
		String v2=s2.substring(s2.lastIndexOf('/')+1,s2.lastIndexOf('.'));
		return GetSecond(v1)<GetSecond(v2);
	}
	public static double GetSecond(String time)
	{
		double year=Double.parseDouble(time.substring(0, 4))*12*30*24*60*60;
		double month=Double.parseDouble(time.substring(4, 6))*30*24*60*60;
		double day=Double.parseDouble(time.substring(6, 8))*24*60*60;
		double hour=Double.parseDouble(time.substring(8, 10))*60*60;
		double minute=Double.parseDouble(time.substring(10, 12))*60;
		double second=Double.parseDouble(time.substring(12, 14));
		return year+month+day+hour+minute+second;
	}*/
	
}
