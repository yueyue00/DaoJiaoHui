package com.dxmap.indoornavig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.ZipException;

import com.dxmap.indoornavig.test.TestPoint;
import com.dxmap.indoornavig.test.UnZipUtils;
import com.fengmap.android.analysis.navi.FMNaviAnalyser;
import com.fengmap.android.analysis.navi.FMNaviModule;
import com.fengmap.android.analysis.navi.FMNaviPrediction;
import com.fengmap.android.analysis.navi.FMNaviResult;
import com.fengmap.android.analysis.search.FMSearchAnalyser;
import com.fengmap.android.analysis.search.FMSearchResult;
import com.fengmap.android.analysis.search.model.FMSearchModelByIdRequest;
import com.fengmap.android.analysis.search.model.FMSearchModelByKeywordRequest;
import com.fengmap.android.analysis.search.model.FMSearchModelByTypeRequest;
import com.fengmap.android.data.FMMapDataManager;
import com.fengmap.android.map.FMGroupInfo;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.layer.FMImageLayer;
import com.fengmap.android.map.layer.FMLineLayer;
import com.fengmap.android.map.marker.FMImageMarker;
import com.fengmap.android.map.marker.FMLineMarker;
import com.fengmap.android.map.marker.FMSegment;
import com.fengmap.android.map.style.FMImageMarkerStyle;
import com.fengmap.android.map.style.FMLineMarkerStyle;
import com.fengmap.android.map.style.FMStyle;
import com.fengmap.android.utils.FMFileUtils;

import android.content.Context;
import android.os.Environment;

public class MapDealUtils {
	
	public static String getDefaultPath(Context context) {
		String m_strAppPath;
		// ��ó��򱣴�����·��
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) // sdcard����
		{
			File sdDir = Environment.getExternalStorageDirectory();
			m_strAppPath = sdDir.toString();

		} else {
			File sdDir = Environment.getDataDirectory();
			m_strAppPath = sdDir.toString();
			m_strAppPath += "/data/";
			m_strAppPath += context.getPackageName();
			m_strAppPath += "/files";
		}
		m_strAppPath += "/dxmap/";
		return m_strAppPath;
	}
	
	 /**
     * ���ͼƬ��ע�
     * @param layer     ͼ��
     * @param position  λ��
     * @param picName   ��ע��ͼƬ����
     * @param size      ��ע���С
     * @return ��ע��
     */
    public static FMImageMarker addImageMarker(FMImageLayer layer,
                                        FMMapCoord position,
                                        String picName,
                                        int size) {
        FMImageMarker      m = new FMImageMarker(position);
        FMImageMarkerStyle s = new FMImageMarkerStyle();
        s.setFMNodeOffsetType(FMStyle.FMNodeOffsetType.FMNODE_MODEL_ABOVE);
        s.setImageFromAssets(picName);
        if (size<=0) {
            size = 40;
        }
        s.setWidth(size);
        s.setHeight(size);
        m.setStyle(s);
        layer.addMarker(m);
        return m;
    }
    
    /**
     * ���ͼƬ��ע�
     * @param layer     ͼ��
     * @param position  λ��
     * @param picName   ��ע��ͼƬ����
     * @param size      ��ע���С
     * @return ��ע��
     */
    public static FMImageMarker addImageMarker(FMImageLayer layer,
                                        FMMapCoord position,
                                        int resid,
                                        int size) {
        FMImageMarker      m = new FMImageMarker(position);
        FMImageMarkerStyle s = new FMImageMarkerStyle();
        s.setFMNodeOffsetType(FMStyle.FMNodeOffsetType.FMNODE_MODEL_ABOVE);
        s.setImageFromRes(resid);
        if (size<=0) {
            size = 40;
        }
        s.setWidth(size);
        s.setHeight(size);
        m.setStyle(s);
        layer.addMarker(m);
        return m;
    }
	
	 /**
     * ��������(ͬ���ߡ������)��
     * @param pNaviResults ·���滮���
     * @param pLayer       ��ͼ��
     * @param lineWidth   �߿�
     * @param lineColor   ����ɫ
     * @return �߶���
     */
    public static FMLineMarker clearAndReDrawLine(FMMap mMap ,ArrayList<FMNaviResult> pNaviResults,
                                   FMLineLayer pLayer,
                                   float lineWidth,
                                   int lineColor) {
        // �����
        pLayer.removeAll();
        mMap.updateMap();

        if (pNaviResults.isEmpty()) {
            return null;
        }

        // �ٻ���
        int[] displayGroupIds = mMap.getDisplayGroupIds();
        int length = displayGroupIds.length;
        if (length == 1) {   // ����
            int current = displayGroupIds[0];
    //        return drawFloorLine(pNaviResults, pLayer, lineWidth, lineColor);
            for (FMNaviResult nr : pNaviResults) {
                if (nr.getGroupId() == current) {
                    if (nr.getPointList().size()>1) {
                        // ��������
                        return drawFloorLine(nr, pLayer, lineWidth, lineColor);
                    }
                }
            }
        } else {  // ���
            // ��������
            return drawSceneLine(pNaviResults, pLayer, lineWidth, lineColor);
        }
        return null;
    }
    
    /**
     * ��������(ͬ���ߡ������)��
     * @param pNaviResults ·���滮���
     * @param pLayer       ��ͼ��
     * @param lineWidth   �߿�
     * @param lineColor   ����ɫ
     * @return �߶���
     */
    public static FMLineMarker clearAndReDrawLineNo(FMMap mMap ,ArrayList<FMNaviResult> pNaviResults,
                                   FMLineLayer pLayer,
                                   float lineWidth,
                                   int lineColor) {
        // �����
        pLayer.removeAll();
        mMap.updateMap();

        if (pNaviResults.isEmpty()) {
            return null;
        }
    //    return drawSceneLine(pNaviResults, pLayer, lineWidth, lineColor);
        // �ٻ���
        int[] displayGroupIds = mMap.getDisplayGroupIds();
        int length = displayGroupIds.length;
        if (length == 1) {   // ����
            int current = displayGroupIds[0];
    //        return drawFloorLine(pNaviResults, pLayer, lineWidth, lineColor);
            ArrayList<FMNaviResult> list = new ArrayList<FMNaviResult>();
            for (FMNaviResult nr : pNaviResults) {
                if (nr.getGroupId() == current) {
                	list.add(nr);
                }
            }
            return drawSceneLine(list, pLayer, lineWidth, lineColor);
        } else {  // ���
            // ��������
            return drawSceneLine(pNaviResults, pLayer, lineWidth, lineColor);
        }
      //  return null;
    }

    /**
     * ��������(ͬ���ߡ������)��
     * @param naviResults ·���滮���
     * @param layer       ��ͼ��
     * @param lineWidth   �߿�
     * @param lineColor   ����ɫ
     * @return �߶���
     */
    public static FMLineMarker drawSceneLine(ArrayList<FMNaviResult> naviResults,
                                      FMLineLayer layer,
                                      float lineWidth,
                                      int lineColor) {

        FMLineMarkerStyle lineStyle = new FMLineMarkerStyle();
        lineStyle.setFillColor(lineColor);
        lineStyle.setLineWidth(lineWidth);
        lineStyle.setLineMode(FMLineMarker.LineMode.FMLINE_PIXEL);
        lineStyle.setLineType(FMLineMarker.LineType.FMLINE_DASHED);

        FMLineMarker lineMarker = new FMLineMarker();
        lineMarker.setStyle(lineStyle);
        for (FMNaviResult nr : naviResults) {
        	FMSegment s = new FMSegment(nr.getGroupId(), nr.getPointList());
            lineMarker.addSegment(s);
        }
        layer.addMarker(lineMarker);
        return lineMarker;
    }
    
    /**
     * ��һ���·���ߡ�
     * @param layer         ͼ��
     * @param lineWidth     �߿�
     * @param lineArrowPic  ���ϵļ�ͷͼƬ����
     * @param lineColor     �ߵ���ɫ
     * @param naviResult    һ������ݽ��
     * @return �߶���
     */
    FMLineMarker drawFloorLine(FMLineLayer layer,
                                      float lineWidth,
                                      String lineArrowPic,
                                      int lineColor,
                                      FMNaviResult naviResult) {

        FMLineMarkerStyle lineStyle = new FMLineMarkerStyle();
        lineStyle.setFillColor(lineColor);
        lineStyle.setLineWidth(lineWidth);
        lineStyle.setImageFromAssets(lineArrowPic);
        lineStyle.setLineMode(FMLineMarker.LineMode.FMLINE_PLANE);
        lineStyle.setLineType(FMLineMarker.LineType.FMLINE_DASHED);


        FMLineMarker floorLine = new FMLineMarker();
        floorLine.setStyle(lineStyle);

        FMSegment s = new FMSegment(naviResult.getGroupId(), naviResult.getPointList());
        floorLine.addSegment(s);

        floorLine.setFMLineDrawType(FMLineMarker.FMLINE_FLOOR);

        layer.addMarker(floorLine);

        return floorLine;
    }
    
    /**
     * ��һ���·���ߡ�
     * @param naviResult    һ������ݽ��
     * @param layer         ͼ��
     * @param lineWidth     �߿�
     * @param lineColor     �ߵ���ɫ
     * @return �߶���
     */
    public static FMLineMarker drawFloorLine(FMNaviResult naviResult,
                                      FMLineLayer layer,
                                      float lineWidth,
                                      int lineColor) {
        FMLineMarkerStyle lineStyle = new FMLineMarkerStyle();
        lineStyle.setFillColor(lineColor);
        lineStyle.setLineWidth(lineWidth);
        lineStyle.setLineMode(FMLineMarker.LineMode.FMLINE_PIXEL);
        lineStyle.setLineType(FMLineMarker.LineType.FMLINE_DASHED);

        FMLineMarker floorLine = new FMLineMarker();
        floorLine.setStyle(lineStyle);

        FMSegment s = new FMSegment(naviResult.getGroupId(), naviResult.getPointList());
        floorLine.addSegment(s);

        floorLine.setFMLineDrawType(FMLineMarker.FMLINE_FLOOR);

        layer.addMarker(floorLine);
        return floorLine;
    }
    /**
     * ��һ���·���ߡ�
     * @param naviResult    һ������ݽ��
     * @param layer         ͼ��
     * @param lineWidth     �߿�
     * @param lineColor     �ߵ���ɫ
     * @return �߶���
     */
    public static FMLineMarker drawFloorLine(ArrayList<FMNaviResult> naviResult,
                                      FMLineLayer layer,
                                      float lineWidth,
                                      int lineColor) {
        FMLineMarkerStyle lineStyle = new FMLineMarkerStyle();
        lineStyle.setFillColor(lineColor);
        lineStyle.setLineWidth(lineWidth);
        lineStyle.setLineMode(FMLineMarker.LineMode.FMLINE_PIXEL);
        lineStyle.setLineType(FMLineMarker.LineType.FMLINE_DASHED);

        FMLineMarker floorLine = new FMLineMarker();
        floorLine.setStyle(lineStyle);

        for(FMNaviResult result : naviResult){
        	FMSegment s = new FMSegment(result.getGroupId(), result.getPointList());
            floorLine.addSegment(s);
        }

        floorLine.setFMLineDrawType(FMLineMarker.FMLINE_FLOOR);

        layer.addMarker(floorLine);
        return floorLine;
    }
    
    /**
     * д���ͼ�ļ���SD�����档
     * @param c             �����Ļ���
     * @param dstFileName   д���ļ�������
     * @param srcAssetsPath Դ�ļ���·��
     */
    public static void writeMap(Context c, String dstDir, String dstFileName,String srcAssetsPath) {
     	   File f = new File(dstDir);
     	   if (!f.exists()){   
     		   f.mkdirs();
     	   } 
     	   
     	   File ff = new File(dstDir,dstFileName);
     	   if(ff.exists()) {
     		   return;
     	   }
     	   //�ļ�������  ��ʱΪ�µĵ�ͼ�ļ�  ��Ŀ¼�ļ�ɾ�����µ���
     	   File[] files = f.listFiles();
     	   for(File file : files){
     		  file.delete();
     	   }
     	  writeFile(c, ff, srcAssetsPath);
    }
    
	public static void writeMap(Context c, String dstDir) {
		File f = new File(dstDir);
		if (!f.exists()) {
			f.mkdirs();
		}
		
		try {
			String[] files = c.getAssets().list("");
			for(String file : files){
				if(file.contains(".fmap")){
					writeMap(c, dstDir, file, file);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeTheme(Context c, String dstDir) {
		File f = new File(dstDir);
		if (!f.exists()) {
			f.mkdirs();
		}
		
		try {
			String[] files = c.getAssets().list("");
			for(String file : files){
				if(file.contains(".theme")){
					writeTheme(c, dstDir, file, file);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	public static void writeTheme(Context c, String dstDir, String dstFileName, String srcAssetsPath) {
		File f = new File(dstDir);
		if (!f.exists()) {
			f.mkdirs();
		}

		File ff = new File(dstDir, dstFileName);
		if (ff.exists()) {
			return;
		}
		// �ļ������� ��ʱΪ�µĵ�ͼ�ļ� ��Ŀ¼�ļ�ɾ�����µ���
		File[] files = f.listFiles();
		for (File file : files) {
			file.delete();
		}
		writeFile(c, ff, srcAssetsPath);
		
		try {
			String[] themes = c.getAssets().list("theme");
			for(String ss : themes){
				ff = new File(dstDir,ss);
				writeFile(c, ff, "theme/"+ss);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
	private static boolean writeFile(Context c, File ff, String srcAssetsPath) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = c.getAssets().open(srcAssetsPath);
			fos = new FileOutputStream(ff);
			byte[] buffer = new byte[10240];
			int byteCount = 0;
			while ((byteCount = is.read(buffer)) != -1) {// ѭ������������ȡ buffer�ֽ�
				fos.write(buffer, 0, byteCount);// ����ȡ��������д�뵽�����
			}
			fos.flush();// ˢ�»�����
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (is != null)
					is.close();
				if (fos != null)
					fos.close();
			} catch (Exception e2) {
				return false;
			}
		}
		return true;
	}
    
    public static ArrayList<TestPoint> getTestPoints(){
    	ArrayList<TestPoint> list = new ArrayList<TestPoint>();
    	TestPoint point = new TestPoint();
    	point.parse(3, new FMMapCoord(1.29388679239967E7, 4853210.10591276));
    	list.add(point);
    	
    	point = new TestPoint();
    	point.parse(3, new FMMapCoord(1.29388239560958E7, 4853119.28126895));
    	list.add(point);
    	
    	/*point = new TestPoint();
    	point.parse(4, new FMMapCoord(1.29388364527513E7, 4853184.59268095));
    	list.add(point);*/
    	
    	point = new TestPoint();
    	point.parse(3, new FMMapCoord(1.29389928568219E7, 4853165.10544963));
    	list.add(point);
    	
    	
    	point = new TestPoint();
    	point.parse(3, new FMMapCoord(1.29389529002308E7, 4853204.96872722));
    	list.add(point);
    	
    	point = new TestPoint();
    	point.parse(4, new FMMapCoord(1.29388797491801E7, 4853219.31607642));
    	list.add(point);
    	
    	point = new TestPoint();
    	point.parse(5, new FMMapCoord(1.29389322848194E7, 4853201.60424424));
    	list.add(point);
    	
    	point = new TestPoint();
    	point.parse(4, new FMMapCoord(1.2938961E7, 4853161.0));
    	list.add(point);
    	
    	point = new TestPoint();
    	point.parse(5, new FMMapCoord(1.2938826383437E7, 4853181.08685798));
    	list.add(point);
    	
    	point = new TestPoint();
    	point.parse(6, new FMMapCoord(1.29388306970246E7, 4853116.759189));
    	list.add(point);
    	
    	point = new TestPoint();
    	point.parse(5, new FMMapCoord(1.29388116892182E7, 4853162.84059968));
    	list.add(point);
    	
    	return list;
    }
    
    public static void QuickSort(ArrayList<TestPoint> list){
    	_quickSort(list,0,list.size()-1);
    }
    
    public static void _quickSort(ArrayList<TestPoint> list, int low, int high) {     
    	  
        if (low < high) {     

           int middle = getMiddle(list, low, high);  //��list�������һ��Ϊ��     

            _quickSort(list, low, middle - 1);        //�Ե��ֱ���еݹ�����     

           _quickSort(list, middle + 1, high);       //�Ը��ֱ���еݹ�����     

        }     

    }   
    
	public static int getMiddle(ArrayList<TestPoint> list, int low, int high) {

		TestPoint tmp = list.get(low); // ����ĵ�һ����Ϊ����

		while (low < high) {

			while (low < high && list.get(high).mGid >= tmp.mGid) {

				high--;

			}

			list.set(low, list.get(high)); // ������С�ļ�¼�Ƶ��Ͷ�

			while (low < high && list.get(low).mGid <= tmp.mGid) {
				low++;
			}
			list.set(high, list.get(low)); // �������ļ�¼�Ƶ��߶�
		}
		list.set(low, tmp); // �����¼��β
		return low; // ���������λ��
	}
	/**
	 * ��ȡ��ʼ��ͼ��ϵ�����̵Ľ��
	 * @param point ��ʼ��
	 * @param list ���ϵ�
	 * @param naviAnalyser
	 * @return
	 */
	public static void getResult(FMMap map, TestPoint point, ArrayList<TestPoint> list, FMNaviAnalyser naviAnalyser,
			ArrayList<FMNaviResult> allresults) {
		ArrayList<FMNaviResult> results = new ArrayList<FMNaviResult>();
		ArrayList<FMNaviResult> tempResults = new ArrayList<FMNaviResult>();
		int index = 0;
		double minlength = 0;
		double length = 0;
		for (int i = 0; i < list.size(); i++) {
			// if(i==2){
			naviAnalyser = FMNaviAnalyser.init(map);
			int type = naviAnalyser.analyzeNavi(point.mGid, point.mCoord, list.get(i).mGid, list.get(i).mCoord,
					FMNaviModule.MODULE_BEST);
			if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_SUCCESS) {
				tempResults.clear();
				tempResults = naviAnalyser.getNaviResults();
				length = naviAnalyser.getFloorRouteLength();
				if (results.size() == 0) {
					results.addAll(tempResults);
					index = i;
					minlength = length;
				} else if (length < minlength) {
					results.clear();
					results.addAll(tempResults);
					index = i;
					minlength = length;
				}
			} else {
				System.out.println("�滮������");
			}
		}

		// }
		System.out.println("size=" + list.size() + ",i=" + index);
		if (results != null) {
			// if (list.size() == 3)
			allresults.addAll(results);
			point.parse(list.get(index));
			list.remove(index);
			if (list.size() > 0)
				getResult(map, point, list, naviAnalyser, allresults);
		}
	}
	
	public static FMNaviAnalyser PointPlan(FMNaviAnalyser analyser,TestPoint sp,TestPoint ep){
		int type = analyser.analyzeNavi(sp.mGid, sp.mCoord, ep.mGid, ep.mCoord,
				FMNaviModule.MODULE_BEST);
		if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_SUCCESS) {
			return analyser;
		}else{
			System.out.println("�滮������");
		}
		return null;
	}
	
	/**
	 * ��ȡ�������ȫ������
	 * @param list
	 * @return
	 */
	private static double getLength(ArrayList<FMNaviResult> list){
		double len = 0;
		for (int i = 0; i < list.size(); i++) {
			len += list.get(i).getLength();
		}
		return len;
	}
	
	public static ArrayList<FMNaviResult> getAllResults(FMMap map,TestPoint point,ArrayList<TestPoint> list,FMNaviAnalyser naviAnalyser){
		ArrayList<FMNaviResult> arrayList = new ArrayList<FMNaviResult>();
		TestPoint tempPoint = new TestPoint();
		tempPoint.parse(point);
		ArrayList<TestPoint> tempList = new ArrayList<TestPoint>();
		int floor = point.mGid;
		for (int i = 0; i < list.size(); i++) {
			if(floor == list.get(i).mGid){
				tempList.add(list.get(i));
			}else if(list.get(i).mGid > floor){
				floor = list.get(i).mGid;
				getResult(map,tempPoint, tempList, naviAnalyser, arrayList);
				tempList.clear();
				tempList.add(list.get(i));
			}
			if(i == list.size() - 1)
				getResult(map,tempPoint, tempList, naviAnalyser, arrayList);
		}
//		getResult(map,tempPoint, list, naviAnalyser, arrayList);
		return arrayList;
	}
	/**
	 * ͨ��fid����ȡ����
	 * @param fid 
	 * @param gids  ¥����
	 * @param searchAnalyser  ������
	 * @return
	 */
	public static FMMapCoord getFMMapCoord(String fid,int[] gids,FMSearchAnalyser searchAnalyser){
		ArrayList<FMSearchResult> resultSet = null;
		int groupid = 0;
		for (int i = 0; i < gids.length; i++) {
			FMSearchModelByIdRequest idReq = new FMSearchModelByIdRequest (gids[i], fid); 
			resultSet = searchAnalyser.executeFMSearchRequest(idReq);
			if(resultSet.size() > 0){
				groupid = gids[i];
				break;
			}
		}
		FMMapCoord coord = null;
		if(resultSet.size() > 0){
			int eid = (Integer) resultSet.get(0).get("eid");
			coord =  searchAnalyser.getModelCoord(groupid, eid); 
		}
		
		return coord;
	}
	/**
	 * ͨ��fid��ȡ���������
	 * @param fid
	 * @param gids  ¥����
	 * @param searchAnalyser ������
	 * @return
	 */
	public static ArrayList<FMSearchResult> getFMResultForFid(String fid,int[] gids,FMSearchAnalyser searchAnalyser){
		ArrayList<FMSearchResult> resultSet = null;
		for (int i = 0; i < gids.length; i++) {
			FMSearchModelByIdRequest idReq = new FMSearchModelByIdRequest (gids[i], fid); 
			resultSet = searchAnalyser.executeFMSearchRequest(idReq);
			
			if(resultSet.size() > 0){
				FMSearchResult result = resultSet.get(0);
				result.put("gid", gids[i]);
				break;
			}
		}
		return resultSet;
	}
    /**
     * ͨ��fid����¥����Ϣ
     * @param fid
     * @param infos  ¥����
     * @param searchAnalyser  ������
     * @return
     */
	public static FMGroupInfo getGroupInfo(String fid,ArrayList<FMGroupInfo> infos,FMSearchAnalyser searchAnalyser){
		ArrayList<FMSearchResult> resultSet = null;
		for (int i = 0; i < infos.size(); i++) {
			FMSearchModelByIdRequest idReq = new FMSearchModelByIdRequest (infos.get(i).getGroupId(), fid); 
			resultSet = searchAnalyser.executeFMSearchRequest(idReq);
			if(resultSet.size() > 0){
				return infos.get(i);
			}
		}
		return null;
	}
	/**
	 * �ؼ���ģ����ѯ
	 * @param key  �ؼ���
	 * @param gids  ¥����
	 * @param searchAnalyser  ������
	 * @return
	 */
	public static ArrayList<FMSearchResult> SearchForKey(String key,int[] gids,FMSearchAnalyser searchAnalyser){
		ArrayList<FMSearchResult> arrayList = new ArrayList<FMSearchResult>();
		for(int i=0;i<gids.length;i++){
			ArrayList<FMSearchResult> searchResults = searchAnalyser.executeFMSearchRequest(new FMSearchModelByKeywordRequest(gids[i], key));
			if(searchResults.size() > 0){
				arrayList.addAll(searchResults);
			}
		}
		return arrayList;
	}
	/**
	 * ���Ͳ�ѯ
	 * @param type  ������
	 * @param gids  ¥����
	 * @param searchAnalyser  ¥����
	 * @return
	 */
	public static ArrayList<FMSearchResult> SearchForType(long type,int[] gids,FMSearchAnalyser searchAnalyser){
		ArrayList<FMSearchResult> arrayList = new ArrayList<FMSearchResult>();
		for(int i=0;i<gids.length;i++){
			ArrayList<FMSearchResult> searchResults = searchAnalyser.executeFMSearchRequest(new FMSearchModelByTypeRequest(gids[i], type));
			if(searchResults.size() > 0){
				arrayList.addAll(searchResults);
			}
		}
		return arrayList;
	}
	
	public static ArrayList<FMSearchResult> SortNo(ArrayList<FMSearchResult> list){
		
		ArrayList<FMSearchResult> results = new ArrayList<FMSearchResult>();
		ArrayList<FMSearchResult> tempResults = new ArrayList<FMSearchResult>();
		for (int i = 0; i < list.size(); i++) {
			FMSearchResult rs = list.get(i);
			if(i == 0){
				tempResults.add(rs);
			}else{
				if(!rs.get("gid").toString().equals(list.get(i-1).get("gid").toString())){
					Collections.sort(tempResults,new Comparator<FMSearchResult>(){

						@Override
						public int compare(FMSearchResult lhs, FMSearchResult rhs) {
							// TODO Auto-generated method stub
							return lhs.get("no").toString().compareTo(rhs.get("no").toString());
						}
					});
					results.addAll(tempResults);
					tempResults.clear();
					tempResults.add(rs);
				}else{
					tempResults.add(rs);
				}
			}
		}
		if(!tempResults.isEmpty()){
			if(tempResults.size() == 1){
				results.addAll(tempResults);
			}else{
				Collections.sort(tempResults,new Comparator<FMSearchResult>(){

					@Override
					public int compare(FMSearchResult lhs, FMSearchResult rhs) {
						// TODO Auto-generated method stub
						return lhs.get("no").toString().compareTo(rhs.get("no").toString());
					}
				});
				results.addAll(tempResults);
				tempResults.clear();
			}
		}
		for (int i = 0; i < list.size(); i++) {
			FMSearchResult r1 = list.get(i);
			FMSearchResult r2 = results.get(i);
			System.out.println("����ǰ: "+r1.get("no").toString()+"   ����� "+r2.get("no").toString());
		}
		return results;
	}
	
	public static String getIndexOfNo(int index) {
		switch (index) {
		case 0:

			return "A";
		case 1:

			return "B";
		case 2:

			return "C";
		case 3:

			return "D";
		case 4:

			return "E";
		default:
			return "";
		}
	}
}
