# 网络请求类库
![](s.png)![](j.png)![](y.png)
### 基本介绍
####  本网络请求库基于OKHttp，retrofit。（Okhttp和retrofit有多强大我想大家都懂）该类库还在优化升级中
       1.支持api信息，log查看，极大的提高了调试接口的效率。错误返回，json格式一目了然。
       2.支持网络数据高度定制缓存策略，支持为每一个接口单独缓存设置，同时支持get和post请求。
       （okhttp仅支持get请求缓存）
       3.数据库缓存，文件缓存。
       4.http请求线程控制

### 如何使用

#### Android Studio
    第一步：
      在项目的gradle里配置
      allprojects {
      		repositories {
      			...
      			maven { url 'https://jitpack.io' }
      		}
      	}

      第二步：
      在module的gradle里配置
      dependencies {
      	        compile 'com.github.shajinyang:SjyNetHelper:1.0.1'
      	}

      	第三步：
      	在自己的application里初始化
      	NetHelper.init(this,"http://api.test.cn/");

        http://api.test.cn/可以用你自己的baseurl



### 使用示例

#### 新建api接口（同retrofit）
    public interface ApiService {

         @FormUrlEncoded
         @POST("index/login/login")
         Observable<HttpResult> login(@FieldMap Map<String,Object> parms);

     }
    这里的HttpResult为网络response接收,一般根据自己的业务需求自行定义泛型。
    public class HttpResult<T> {
       public String error;
       public String info;
       public T data;
    }

#### 发起请求
    public void getNet(){

            HashMap<String,Object> map=new HashMap<>();
            map.put("params","2");
            map.put("params2","1");
            map.put("params3","1");

            NetHelper.getInstance()
                    .isCache(true)//可不设置默认不缓存
                    .cacheExpire(60*1000)//可不设置默认缓有效期1分钟
                    .create(ApiService.class)
                    .login(map)
                    .observeOn(AndroidSchedulers.mainThread())//线程控制
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BaseSubscriber<HttpResult<T>>() {
                        @Override
                        public void onStartNet() {
                            //todo 请求开始
                        }

                        @Override
                        public void onErrorNet(Throwable e) {
                            //todo 请求错误
                        }

                        @Override
                        void onCompeteReq() {
                            //todo 请求结束
                        }

                        @Override
                        void onNextReq(HttpResult<T> h) {
                            //todo 处理数据
                        }

                    });

        }

        这里BaseSubscriber可以自己定义，如果你需要对返回参数统一处理，可以
        自定义YourSubscriber 继承BaseSubscriber,然后在YourSubscriber中处理
        你自己的逻辑。

#### 其他说明
    有时候特殊的业务需求，可能会有多个baseurl ，可以在apiservice 的url注解添加全路径









