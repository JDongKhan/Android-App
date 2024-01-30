package com.jd.core.utils;

import android.net.Uri;
import android.os.AsyncTask;

import com.jd.core.log.LogUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class Domain2IP {

    public static boolean isRunning = false;

    public static String ipAddressArray;

    public static String parserHost(String url) {
        Uri uri = Uri.parse(url);

        if (isRunning) {
            return ipAddressArray;
        }
        isRunning = true;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        AsyncTask<String, Integer, String> execute = myAsyncTask.execute(uri.getHost());
        String s = null;
        try {
            s = execute.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        isRunning = false;
        ipAddressArray = s;
        return s;

    }

    public static class MyAsyncTask extends AsyncTask<String, Integer, String> {
        public String ipAddress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return parseHostGetIPAddress(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            this.ipAddress = s;
        }

        /**
         * 解析域名获取IP数组
         * @param host
         * @return
         */
        public String parseHostGetIPAddress(String host) {
            StringBuffer ipAddressArr = new StringBuffer();
            try {
                InetAddress[] inetAddressArr = InetAddress.getAllByName(host);
                if (inetAddressArr != null && inetAddressArr.length > 0) {
                    for (int i = 0; i < inetAddressArr.length; i++) {
                        ipAddressArr.append(inetAddressArr[i].getHostAddress());
                        ipAddressArr.append(",");
                    }
                }
            } catch (UnknownHostException e) {
                LogUtils.e("network",e.getMessage());
                return null;
            }
            if (ipAddressArr.length() > 1) {
                return ipAddressArr.substring(0,ipAddressArr.length()-1);
            }
            return ipAddressArr.toString();
        }
    }
}
