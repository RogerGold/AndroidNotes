# android日期显示格式
  
      1. 英文显示格式
      SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd");//注意有3个M
      textView.setText(format.format(calendar.getTime()));
      这样显示是 2017 Feb 01
      SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd");//注意只有2个M
      textView.setText(format.format(calendar.getTime()));
      这样显示是 2017 01 01
      
      SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    HH:mm:ss     ");       
      Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
      String    str    =    formatter.format(curDate); 
      
      2.判断12小时还是24小时制
      String strTimeFormat = android.provider.Settings.System.getString(cv,  
                                          android.provider.Settings.System.TIME_12_24);  
        
       if(strTimeFormat.equals("24"))  {  
              Log.i("activity","24");  
       }   
      
      3.使用日历获取：
          Calendar c = Calendar.getInstance();  
    取得系统日期:year = c.get(Calendar.YEAR)  
                   month = c.grt(Calendar.MONTH)  
                   day = c.get(Calendar.DAY_OF_MONTH)  
    取得系统时间：hour = c.get(Calendar.HOUR_OF_DAY);  
                      minute = c.get(Calendar.MINUTE)  
                        Calendar c = Calendar.getInstance();  
    取得系统日期:year = c.get(Calendar.YEAR)  
                       month = c.grt(Calendar.MONTH)  
                       day = c.get(Calendar.DAY_OF_MONTH)  
    取得系统时间：hour = c.get(Calendar.HOUR_OF_DAY);  
                         minute = c.get(Calendar.MINUTE) 
