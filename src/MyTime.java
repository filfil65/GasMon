import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MyTime {
	int minute = 60000;
	public static void getMinutes(){
		
	Calendar calendar = new GregorianCalendar();
	
	LocalDateTime now = LocalDateTime.now();  
	now.plusMinutes(1);
	System.out.println(now  + "now");
	System.out.println(now.plusMinutes(1) + "now + 1");
	now.plusMinutes(1);

	System.out.println(Calendar.getInstance().get(Calendar.MILLISECOND));
	System.out.println(System.currentTimeMillis());
	
    calendar.setTimeInMillis(1427723278405L);
    //calendar.setTimeInMillis(now)
    
    ZonedDateTime utc = Instant.ofEpochMilli(1427723278405L).atZone(ZoneOffset.UTC);
    System.out.println(utc);


    DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");

    formatter.setCalendar(calendar);

    System.out.println(formatter.format(calendar.getTime()));

    formatter.setTimeZone(TimeZone.getTimeZone("Britain"));

    System.out.println(formatter.format(calendar.getTime()));
	}
}
