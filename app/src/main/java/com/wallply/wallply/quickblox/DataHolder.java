package com.wallply.wallply.quickblox;

import android.util.SparseArray;

import com.quickblox.content.model.QBFile;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by sree on 2/23/2017.
 */

public class DataHolder {
    private static DataHolder instance;


    private ArrayList<QBFile> natureArrayList;
    private ArrayList<QBFile> cityArrayList;
    private ArrayList<QBFile> patternsArrayList;
    private ArrayList<QBFile> colorfulArrayList;
    private ArrayList<QBFile> amoledArrayList;
    private ArrayList<QBFile> gradientArrayList;
    private ArrayList<QBFile> materialArrayList;
    private ArrayList<QBFile> carsArrayList;
    private ArrayList<QBFile> arrayList;

    private DataHolder() {
        arrayList = new ArrayList<QBFile>();
        natureArrayList = new ArrayList<QBFile>();
        cityArrayList = new ArrayList<QBFile>();
        patternsArrayList = new ArrayList<QBFile>();
        colorfulArrayList = new ArrayList<QBFile>();
        amoledArrayList = new ArrayList<QBFile>();
        gradientArrayList = new ArrayList<QBFile>();
        materialArrayList = new ArrayList<QBFile>();
        carsArrayList = new ArrayList<QBFile>();
        //qbFiles = new SparseArray<>();
    }

    public static synchronized DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public void addQbFiles(Collection<QBFile> qbFileList) {
        for (QBFile qbFile : qbFileList)
        {
            addQbFile(qbFile);

        }
    }

    public ArrayList<QBFile> getQBFiles() {
        return arrayList;
    }


    public ArrayList<QBFile> getQBFiles(String category) {

        switch (category){
            case "gradient":


                return gradientArrayList;

            case "nature":

                return natureArrayList;

            case "city":

                return cityArrayList;

            case "material":

                return materialArrayList;

            case "pattern":

                return patternsArrayList;

            case "color":

                return colorfulArrayList;

            case "amoled":

                return amoledArrayList;
            case "car":

                return carsArrayList;


            default:
                return null;

        }
    }

    public boolean isEmpty() {
        return arrayList.size() == 0;
    }

    public boolean isEmpty(String category) {

        switch (category) {
            case "gradient":


                return gradientArrayList.size() == 0;

            case "nature":

                return natureArrayList.size() == 0;

            case "city":

                return cityArrayList.size() == 0;

            case "material":

                return materialArrayList.size() == 0;

            case "pattern":

                return patternsArrayList.size() == 0;

            case "color":

                return colorfulArrayList.size() == 0;

            case "amoled":

                return amoledArrayList.size() == 0;
            case "car":

                return carsArrayList.size() == 0;


            default:
                return true;
        }


    }

    public void clear() {

        arrayList.clear();
        gradientArrayList.clear();
        materialArrayList.clear();
        patternsArrayList.clear();
        carsArrayList.clear();
        colorfulArrayList.clear();
        amoledArrayList.clear();
        cityArrayList.clear();
    }

    public void addQbFile(QBFile qbFile) {
        arrayList.add(qbFile);
        //qbFiles.put(qbFile.getId(), qbFile);
    }

    public void addQbFile(QBFile qbFile,String category) {
        switch (category){
            case "gradient":


                gradientArrayList.add(qbFile);
                break;
            case "nature":

                natureArrayList.add(qbFile);
                break;
            case "city":

                cityArrayList.add(qbFile);
                break;
            case "material":

                materialArrayList.add(qbFile);
                break;
            case "pattern":

                patternsArrayList.add(qbFile);
                break;
            case "color":

                colorfulArrayList.add(qbFile);
                break;
            case "amoled":

               amoledArrayList.add(qbFile);
                break;
            case "car":

               carsArrayList.add(qbFile);
                break;

            default:
                break;
        }

    }

    public void addQbFileArray(QBFile qbFile) {

    }

    public QBFile getQBFile(int id) {

        return arrayList.get(id);
    }

    public QBFile getQBFile(int id,String category) {


        switch (category){
            case "gradient":


                return gradientArrayList.get(id);

            case "nature":

               return natureArrayList.get(id);

            case "city":

                return cityArrayList.get(id);

            case "material":

               return materialArrayList.get(id);

            case "pattern":

               return patternsArrayList.get(id);

            case "color":

               return colorfulArrayList.get(id);

            case "amoled":

               return amoledArrayList.get(id);

            case "car":

               return carsArrayList.get(id);


            default:
                return null;

        }


    }


    public void clear(String category)
    {

        switch (category){
            case "gradient":


                gradientArrayList.clear();

            case "nature":

                natureArrayList.clear();

            case "city":

                cityArrayList.clear();

            case "material":

                materialArrayList.clear();

            case "pattern":

                patternsArrayList.clear();

            case "color":

                colorfulArrayList.clear();

            case "amoled":

                amoledArrayList.clear();

            case "car":

                carsArrayList.clear();


            default:
               break;

        }

    }


}
