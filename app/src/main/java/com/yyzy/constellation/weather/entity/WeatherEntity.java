package com.yyzy.constellation.weather.entity;

import java.util.List;

//最近五天天气信息
public class WeatherEntity {

    private String reason;
    private ResultDTO result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultDTO getResult() {
        return result;
    }

    public void setResult(ResultDTO result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultDTO {
        private String city;
        private RealtimeDTO realtime;
        private List<FutureDTO> future;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public RealtimeDTO getRealtime() {
            return realtime;
        }

        public void setRealtime(RealtimeDTO realtime) {
            this.realtime = realtime;
        }

        public List<FutureDTO> getFuture() {
            return future;
        }

        public void setFuture(List<FutureDTO> future) {
            this.future = future;
        }

        public static class RealtimeDTO {
            private String temperature;
            private String humidity;
            private String info;
            private String wid;
            private String direct;
            private String power;
            private String aqi;

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getHumidity() {
                return humidity;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getWid() {
                return wid;
            }

            public void setWid(String wid) {
                this.wid = wid;
            }

            public String getDirect() {
                return direct;
            }

            public void setDirect(String direct) {
                this.direct = direct;
            }

            public String getPower() {
                return power;
            }

            public void setPower(String power) {
                this.power = power;
            }

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }
        }

        public static class FutureDTO {
            private String date;
            private String temperature;
            private String weather;
            private WidDTO wid;
            private String direct;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WidDTO getWid() {
                return wid;
            }

            public void setWid(WidDTO wid) {
                this.wid = wid;
            }

            public String getDirect() {
                return direct;
            }

            public void setDirect(String direct) {
                this.direct = direct;
            }

            public static class WidDTO {
                private String day;
                private String night;

                public String getDay() {
                    return day;
                }

                public void setDay(String day) {
                    this.day = day;
                }

                public String getNight() {
                    return night;
                }

                public void setNight(String night) {
                    this.night = night;
                }
            }
        }
    }
}
