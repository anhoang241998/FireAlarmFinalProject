package com.example.firealarmversion13.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.firealarmversion13.model.Room;

import java.util.List;

public class HistoryRepository {
    private ApplicationDao mApplicationDao;

    private LiveData<List<Room>> allHistoryData;
    private  static HistoryDataListener mHistoryDataListener;

    public HistoryRepository(Application application) {
        FireAlarmDatabase database = FireAlarmDatabase.getInstance(application);
        mApplicationDao = database.getApplicationDao();
        allHistoryData = mApplicationDao.getAllHistoryData();
    }

    public void insertHistoryData(Room historyData) {
        new InsertTask(mApplicationDao).execute(historyData);
    }

    public void updateHistoryData(Room historyData) {
        new UpdateTask(mApplicationDao).execute(historyData);
    }

    public void deleteHistoryData(Room historyData) {
        new DeleteTask(mApplicationDao).execute(historyData);
    }

    public LiveData<List<Room>> getAllHistoryData() {
        return allHistoryData;
    }
    public void getAllHistory(Room historyData){
       new AllHistoryTask(mApplicationDao,historyData).execute();
    }
    public void setListener(HistoryDataListener dataListener ){
        mHistoryDataListener =dataListener;
    }

    private static class AllHistoryTask extends AsyncTask<Void, Void, List<Room>> {
        private ApplicationDao mApplicationDao;
        private Room mHistoryData;

        public AllHistoryTask(ApplicationDao applicationDao, Room data) {
            mApplicationDao = applicationDao;
            mHistoryData=data;
        }


        @Override
        protected List<Room> doInBackground(Void... voids) {
            return mApplicationDao.getAllHistory();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(List<Room> historyData) {
            super.onPostExecute(historyData);
            mHistoryDataListener.onGetDataCompleted(historyData,mHistoryData);
        }
    }

    private static class InsertTask extends AsyncTask<Room, Void, Void> {
        private ApplicationDao mApplicationDao;

        public InsertTask(ApplicationDao applicationDao) {
            mApplicationDao = applicationDao;
        }

        @Override
        protected Void doInBackground(Room... historyData) {
            mApplicationDao.insertHistoryData(historyData[0]);
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Room, Void, Void> {
        private ApplicationDao mApplicationDao;

        public UpdateTask(ApplicationDao applicationDao) {
            mApplicationDao = applicationDao;
        }

        @Override
        protected Void doInBackground(Room... historyData) {
            mApplicationDao.updateHistoryData(historyData[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Room, Void, Void> {
        private ApplicationDao mApplicationDao;

        public DeleteTask(ApplicationDao applicationDao) {
            mApplicationDao = applicationDao;
        }

        @Override
        protected Void doInBackground(Room... historyData) {
            mApplicationDao.deleteHistoryData(historyData[0]);
            return null;
        }
    }

}
