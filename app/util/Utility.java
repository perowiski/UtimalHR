package util;

import org.apache.commons.lang3.StringUtils;
import play.mvc.Http;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by peter on 4/21/17.
 */
public class Utility {

    public static List<LocalDate> getDateRange(String from, String to, boolean excludeWeekend){
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);
        List<LocalDate> dateRange = new ArrayList<>();
        while(!startDate.isAfter(endDate)){
            if(excludeWeekend){
                DayOfWeek dayOfWeek = startDate.getDayOfWeek();
                if(dayOfWeek != DayOfWeek.SATURDAY){
                    if(dayOfWeek != DayOfWeek.SUNDAY){
                        dateRange.add(startDate);
                    }
                }
            }else{
                dateRange.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }
        return dateRange;
    }

    public static String getQueryString(String queryParam){
        return Http.Context.current().request().getQueryString(queryParam);
    }

    public static String getUri(){
        return Http.Context.current().request().uri();
    }


    public static String printTime(LocalTime localTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return localTime.format(dateTimeFormatter);
    }

    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if(pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if(sourceList == null || sourceList.size() < fromIndex){
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }


    public static String splitToTimeFormat(String s){
        String hour = s.charAt(0) +""+ s.charAt(1);
        String minute = s.charAt(2) +""+ s.charAt(3);
        String seconds = s.charAt(4) +""+ s.charAt(5);
        return hour + ":" + minute + ":" +seconds;
    }
}
