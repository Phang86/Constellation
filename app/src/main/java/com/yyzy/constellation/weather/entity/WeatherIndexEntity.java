package com.yyzy.constellation.weather.entity;

//天气指数信息
public class WeatherIndexEntity {

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
        private LifeDTO life;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public LifeDTO getLife() {
            return life;
        }

        public void setLife(LifeDTO life) {
            this.life = life;
        }

        public static class LifeDTO {
            private KongtiaoDTO kongtiao;
            private GuominDTO guomin;
            private ShushiduDTO shushidu;
            private ChuanyiDTO chuanyi;
            private DiaoyuDTO diaoyu;
            private GanmaoDTO ganmao;
            private ZiwaixianDTO ziwaixian;
            private XicheDTO xiche;
            private YundongDTO yundong;
            private DaisanDTO daisan;

            public KongtiaoDTO getKongtiao() {
                return kongtiao;
            }

            public void setKongtiao(KongtiaoDTO kongtiao) {
                this.kongtiao = kongtiao;
            }

            public GuominDTO getGuomin() {
                return guomin;
            }

            public void setGuomin(GuominDTO guomin) {
                this.guomin = guomin;
            }

            public ShushiduDTO getShushidu() {
                return shushidu;
            }

            public void setShushidu(ShushiduDTO shushidu) {
                this.shushidu = shushidu;
            }

            public ChuanyiDTO getChuanyi() {
                return chuanyi;
            }

            public void setChuanyi(ChuanyiDTO chuanyi) {
                this.chuanyi = chuanyi;
            }

            public DiaoyuDTO getDiaoyu() {
                return diaoyu;
            }

            public void setDiaoyu(DiaoyuDTO diaoyu) {
                this.diaoyu = diaoyu;
            }

            public GanmaoDTO getGanmao() {
                return ganmao;
            }

            public void setGanmao(GanmaoDTO ganmao) {
                this.ganmao = ganmao;
            }

            public ZiwaixianDTO getZiwaixian() {
                return ziwaixian;
            }

            public void setZiwaixian(ZiwaixianDTO ziwaixian) {
                this.ziwaixian = ziwaixian;
            }

            public XicheDTO getXiche() {
                return xiche;
            }

            public void setXiche(XicheDTO xiche) {
                this.xiche = xiche;
            }

            public YundongDTO getYundong() {
                return yundong;
            }

            public void setYundong(YundongDTO yundong) {
                this.yundong = yundong;
            }

            public DaisanDTO getDaisan() {
                return daisan;
            }

            public void setDaisan(DaisanDTO daisan) {
                this.daisan = daisan;
            }

            public static class KongtiaoDTO {
                private String v;
                private String des;

                public String getV() {
                    return v;
                }

                public void setV(String v) {
                    this.v = v;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }

            public static class GuominDTO {
                private String v;
                private String des;

                public String getV() {
                    return v;
                }

                public void setV(String v) {
                    this.v = v;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }

            public static class ShushiduDTO {
                private String v;
                private String des;

                public String getV() {
                    return v;
                }

                public void setV(String v) {
                    this.v = v;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }

            public static class ChuanyiDTO {
                private String v;
                private String des;

                public String getV() {
                    return v;
                }

                public void setV(String v) {
                    this.v = v;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }

            public static class DiaoyuDTO {
                private String v;
                private String des;

                public String getV() {
                    return v;
                }

                public void setV(String v) {
                    this.v = v;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }

            public static class GanmaoDTO {
                private String v;
                private String des;

                public String getV() {
                    return v;
                }

                public void setV(String v) {
                    this.v = v;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }

            public static class ZiwaixianDTO {
                private String v;
                private String des;

                public String getV() {
                    return v;
                }

                public void setV(String v) {
                    this.v = v;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }

            public static class XicheDTO {
                private String v;
                private String des;

                public String getV() {
                    return v;
                }

                public void setV(String v) {
                    this.v = v;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }

            public static class YundongDTO {
                private String v;
                private String des;

                public String getV() {
                    return v;
                }

                public void setV(String v) {
                    this.v = v;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }

            public static class DaisanDTO {
                private String v;
                private String des;

                public String getV() {
                    return v;
                }

                public void setV(String v) {
                    this.v = v;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }
        }
    }
}
