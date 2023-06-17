package com.trucksoft.isoft.isoft_elog.Isoft_adapter;

import android.util.Log;

import java.util.StringTokenizer;

public class SetTimevalue {
	
	public String getYaxisvalue(String type) {
		String value="";
		if(type.contentEquals("OFF"))
		{
			value="61";
		}else if(type.contentEquals("SB"))
		{
			value="101";
			
		}else if(type.contentEquals("D"))
		{
			//value="135";
			value="135";
		}
		else
		{
			Log.e("ttype","="+type);
			value="170";
			//value="120";
		}
		
        return value;
    }
	public String getYaxisvaluebig(String type) {
		String value="";
		if(type.contentEquals("OFF"))
		{
			value="115";
		}else if(type.contentEquals("SB"))
		{
			value="195";

		}else if(type.contentEquals("D"))
		{
			//value="135";
			value="275";
		}
		else
		{
			Log.e("ttype","="+type);
			//value="170";
			//value="120";
			value="330";
		}

		return value;
	}
	public String getXaxisvaluenew(String ftime)
	{
		StringTokenizer str=new StringTokenizer(ftime,":");
		String fk=str.nextToken();
		String fd=str.nextToken();
		//int fval=Integer.parseInt(fk);
		Double fval=0.0;

		Double dsec=0.00;
		dsec=getXaxisvalueseconds(fd);

		fval= Double.parseDouble(fk);
		String value="";
		Double Total=0.00;
		Total=fval*46.83+64;
		
		value= String.valueOf(Total+dsec);
		 return value;
	}



	public String getXaxisvaluenewdx(String ftime)
	{
		StringTokenizer str=new StringTokenizer(ftime,":");
		String fk=str.nextToken();
		String fd=str.nextToken();
		//int fval=Integer.parseInt(fk);
		Double fval=0.0;

		Double dsec=0.00;
		dsec=getXaxisvalueseconds(fd);

		fval= Double.parseDouble(fk);
		String value="";
		Double Total=0.00;
		Total=fval*46.83+66;

		value= String.valueOf(Total+dsec);
		return value;
	}






	public String getXaxisvaluenew1215(String ftime)
	{
		StringTokenizer str=new StringTokenizer(ftime,":");
		String fk=str.nextToken();
		String fd=str.nextToken();
		//int fval=Integer.parseInt(fk);
		Double fval=0.0;

		Double dsec=0.00;
		dsec=getXaxisvalueseconds(fd);

		fval= Double.parseDouble(fk);
		String value="";
		Double Total=0.00;
		Total=fval*44.50+60;

		value= String.valueOf(Total+dsec);
		return value;
	}
	public String getXaxisvaluenew1250z(String ftime)
	{
		StringTokenizer str=new StringTokenizer(ftime,":");
		String fk=str.nextToken();
		String fd=str.nextToken();
		//int fval=Integer.parseInt(fk);
		Double fval=0.0;

		Double dsec=0.00;
		dsec=getXaxisvalueseconds(fd);
		//Log.e("dsec * dsec","*"+dsec);

		fval= Double.parseDouble(fk);
		String value="";
		Double Total=0.00;
		Total=fval*46.01+60;
		//Log.e("Total * Total","*"+Total);
		value= String.valueOf(Total+dsec);
		//Log.e("value * value","*"+value);
		return value;
	}
	public String getXaxisvaluenew1250(String ftime)
	{
		StringTokenizer str=new StringTokenizer(ftime,":");
		String fk=str.nextToken();
		String fd=str.nextToken();
		//int fval=Integer.parseInt(fk);
		Double fval=0.0;

		Double dsec=0.00;
		dsec=getXaxisvalueseconds(fd);

		fval= Double.parseDouble(fk);
		//Log.e("fval *","Z"+fval);
		String value="";
		Double Total=0.00;
		Total=fval*46.01+60;
		//Log.e("Totalz *","Z"+Total);
		value= String.valueOf(Total+dsec);
		//Log.e("value *","Z"+value);
		return value;
	}

	public String getXaxisvaluenew2000(String ftime)
	{
		StringTokenizer str=new StringTokenizer(ftime,":");
		String fk=str.nextToken();
		String fd=str.nextToken();
		//int fval=Integer.parseInt(fk);
		Double fval=0.0;

		Double dsec=0.00;
		dsec=getXaxisvalueseconds(fd);

		fval= Double.parseDouble(fk);
		String value="";
		Double Total=0.00;
		Total=fval*89.01+115;

		value= String.valueOf(Total+dsec);
		return value;
	}

	public String getXaxisvaluenew1000(String ftime)
	{
		StringTokenizer str=new StringTokenizer(ftime,":");
		String fk=str.nextToken();
		String fd=str.nextToken();
		//int fval=Integer.parseInt(fk);
		Double fval=0.0;

		Double dsec=0.00;
		dsec=getXaxisvalueseconds(fd);

		fval= Double.parseDouble(fk);
		String value="";
		Double Total=0.00;
		Total=fval*43.80+60;

		value= String.valueOf(Total+dsec);
		return value;
	}
	public String getXaxisvaluenew900(String ftime)
	{
		StringTokenizer str=new StringTokenizer(ftime,":");
		String fk=str.nextToken();
		String fd=str.nextToken();
		//int fval=Integer.parseInt(fk);
		Double fval=0.0;

		Double dsec=0.00;
		dsec=getXaxisvalueseconds(fd);

		fval= Double.parseDouble(fk);
		String value="";
		Double Total=0.00;
		Total=fval*41.20+58;

		value= String.valueOf(Total+dsec);
		return value;
	}

	public Double getXaxisvalueseconds(String ftime)
	{

		Double fval=0.0;
		fval= Double.parseDouble(ftime);
		String value="";
		Double Total=0.00;
		Total=fval*0.77;

		//value=String.valueOf(Total);
		return Total;
	}
	
//	public  String getXaxisvalue(String ftime) {
//
//		StringTokenizer str=new StringTokenizer(ftime,":");
//		String fk=str.nextToken();
//		int fval=Integer.parseInt(fk);
//		//each hour decrease 45count
//
//		//24 hour total 1185+4
//		String value="";
//		if(fval>23)
//		{
//			value="1185";
//		}else if(fval>23)
//		{
//			value="1095";
//
//		}else if(fval>22)
//		{
//			value="1050";
//		}
//		else if(fval>21)
//		{
//			value="1005";
//		}
//		else if(fval>20)
//		{
//			value="960";
//		}
//		else if(fval>19)
//		{
//			value="915";
//		}
//		else if(fval>18)
//		{
//			value="870";
//		}
//		else if(fval>17)
//		{
//			value="825";
//		}
//		else if(fval>16)
//		{
//			value="780";
//		}
//
//		else if(fval>15)
//		{
//			value="735";
//		}
//		else if(fval>14)
//		{
//			value="690";
//		}
//		else if(fval>13)
//		{
//			value="645";
//		}
//		else if(fval>12)
//		{
//			value="600";
//		}
//		else if(fval>11)
//		{
//			value="555";
//		}
//		else if(fval>10)
//		{
//			value="510";
//		}
//		else if(fval>9)
//		{
//			value="465";
//		}
//		else if(fval>8)
//		{
//			value="420";
//		}
//		else if(fval>7)
//		{
//			value="375";
//		}
//		else if(fval>6)
//		{
//			value="330";
//		}
//		else if(fval>5)
//		{
//			value="285";
//		}
//		else if(fval>4)
//		{
//			value="240";
//		}
//		else if(fval>3)
//		{
//			value="195";
//		}
//		else if(fval>2)
//		{
//			value="150";
//		}
//		else
//		{
//			value="110";
//		}
//
//        return value;
//    }

}
