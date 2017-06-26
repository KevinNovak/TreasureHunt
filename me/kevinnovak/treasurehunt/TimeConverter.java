package me.kevinnovak.treasurehunt;

public class TimeConverter {
	private String day, days, hour, hours, minute, minutes, second, seconds;
	
	public TimeConverter(String day, String days, String hour, String hours, String minute, String minutes, String second, String seconds) {
		this.day = day;
		this.days = days;
		this.hour = hour;
		this.hours = hours;
		this.minute = minute;
		this.minutes = minutes;
		this.second = second;
		this.seconds = seconds;
	}
	
    String friendlyTime(int initSeconds) {
        String init = "";
        
        // days
        if ((initSeconds/86400) >= 1) {
            int days = initSeconds/86400;
            initSeconds = initSeconds%86400;
            if (days > 1) {
                init = init + " " + days + " " + this.days;
            } else {
                init = init + " " + days + " " + this.day;
            }
        }
        
        // hours
        if ((initSeconds/3600) >= 1) {
            int hours = initSeconds/3600;
            initSeconds = initSeconds%3600;
            if (hours > 1) {
                init = init + " " + hours + " " + this.hours;
            } else {
                init = init + " " + hours + " " + this.hour;
            }
        }
        
        // minutes
        if ((initSeconds/60) >= 1) {
            int minutes = initSeconds/60;
            initSeconds = initSeconds%60;
            if (minutes > 1) {
                init = init + " " + minutes + " " + this.minutes;
            } else {
                init = init + " " + minutes + " " + this.minute;
            }
        }
        
        // seconds
        if (initSeconds >= 1) {
            if (initSeconds > 1) {
                init = init + " " + initSeconds + " " + this.seconds;
            } else {
                init = init + " " + initSeconds + " " + this.second;
            }
        }
        // remove the initial space
        init = init.substring(1, init.length());
        return init;
    }
}