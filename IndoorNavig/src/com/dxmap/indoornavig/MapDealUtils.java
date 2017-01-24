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
		// 获得程序保存数据路径
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) // sdcard存在
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
     * 添加图片标注物。
     * @param layer     图层
     * @param position  位置
     * @param picName   标注物图片名字
     * @param size      标注物大小
     * @return 标注物
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
     * 添加图片标注物。
     * @param layer     图层
     * @param position  位置
     * @param picName   标注物图片名字
     * @param size      标注物大小
     * @return 标注物
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
     * 画场景线(同层线、跨层线)。
     * @param pNaviResults 路径规划结果
     * @param pLayer       线图层
     * @param lineWidth   线宽
     * @param lineColor   线颜色
     * @return 线对象
     */
    public static FMLineMarker clearAndReDrawLine(FMMap mMap ,ArrayList<FMNaviResult> pNaviResults,
                                   FMLineLayer pLayer,
                                   float lineWidth,
                                   int lineColor) {
        // 清除线
        pLayer.removeAll();
        mMap.updateMap();

        if (pNaviResults.isEmpty()) {
            return null;
        }

        // 再画线
        int[] displayGroupIds = mMap.getDisplayGroupIds();
        int length = displayGroupIds.length;
        if (length == 1) {   // 单层
            int current = displayGroupIds[0];
    //        return drawFloorLine(pNaviResults, pLayer, lineWidth, lineColor);
            for (FMNaviResult nr : pNaviResults) {
                if (nr.getGroupId() == current) {
                    if (nr.getPointList().size()>1) {
                        // 画单层线
                        return drawFloorLine(nr, pLayer, lineWidth, lineColor);
                    }
                }
            }
        } else {  // 多层
            // 画多层层线
            return drawSceneLine(pNaviResults, pLayer, lineWidth, lineColor);
        }
        return null;
    }
    
    /**
     * 画场景线(同层线、跨层线)。
     * @param pNaviResults 路径规划结果
     * @param pLayer       线图层
     * @param lineWidth   线宽
     * @param lineColor   线颜色
     * @return 线对象
     */
    public static FMLineMarker clearAndReDrawLineNo(FMMap mMap ,ArrayList<FMNaviResult> pNaviResults,
                                   FMLineLayer pLayer,
                                   float lineWidth,
                                   int lineColor) {
        // 清除线
        pLayer.removeAll();
        mMap.updateMap();

        if (pNaviResults.isEmpty()) {
            return null;
        }
    //    return drawSceneLine(pNaviResults, pLayer, lineWidth, lineColor);
        // 再画线
        int[] displayGroupIds = mMap.getDisplayGroupIds();
        int length = displayGroupIds.length;
        if (length == 1) {   // 单层
            int current = displayGroupIds[0];
    //        return drawFloorLine(pNaviResults, pLayer, lineWidth, lineColor);
            ArrayList<FMNaviResult> list = new ArrayList<FMNaviResult>();
            for (FMNaviResult nr : pNaviResults) {
                if (nr.getGroupId() == current) {
                	list.add(nr);
                }
            }
            return drawSceneLine(list, pLayer, lineWidth, lineColor);
        } else {  // 多层
            // 画多层层线
            return drawSceneLine(pNaviResults, pLayer, lineWidth, lineColor);
        }
      //  return null;
    }

    /**
     * 画场景线(同层线、跨层线)。
     * @param naviResults 路径规划结果
     * @param layer       线图层
     * @param lineWidth   线宽
     * @param lineColor   线颜色
     * @return 线对象
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
     * 画一层的路径线。
     * @param layer         图层
     * @param lineWidth     线宽
     * @param lineArrowPic  线上的箭头图片名称
     * @param lineColor     线的颜色
     * @param naviResult    一层的数据结果
     * @return 线对象
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
     * 画一层的路径线。
     * @param naviResult    一层的数据结果
     * @param layer         图层
     * @param lineWidth     线宽
     * @param lineColor     线的颜色
     * @return 线对象
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
     * 画一层的路径线。
     * @param naviResult    一层的数据结果
     * @param layer         图层
     * @param lineWidth     线宽
     * @param lineColor     线的颜色
     * @return 线对象
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
     * 写入地图文件到SD卡里面。
     * @param c             上下文环境
     * @param dstFileName   写入文件的名字
     * @param srcAssetsPath 源文件的路径
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
     	   //文件不存在  此时为新的地图文件  把目录文件删除重新导入
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
		// 文件不存在 此时为新的地图文件 把目录文件删除重新导入
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
			while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取 buffer字节
				fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
			}
			fos.flush();// 刷新缓冲区
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

           int middle = getMiddle(list, low, high);  //将list数组进行一分为二     

            _quickSort(list, low, middle - 1);        //对低字表进行递归排序     

           _quickSort(list, middle + 1, high);       //对高字表进行递归排序     

        }     

    }   
    
	public static int getMiddle(ArrayList<TestPoint> list, int low, int high) {

		TestPoint tmp = list.get(low); // 数组的第一个作为中轴

		while (low < high) {

			while (low < high && list.get(high).mGid >= tmp.mGid) {

				high--;

			}

			list.set(low, list.get(high)); // 比中轴小的记录移到低端

			while (low < high && list.get(low).mGid <= tmp.mGid) {
				low++;
			}
			list.set(high, list.get(low)); // 比中轴大的记录移到高端
		}
		list.set(low, tmp); // 中轴记录到尾
		return low; // 返回中轴的位置
	}
	/**
	 * 获取起始点和集合点中最短的结果
	 * @param point 起始点
	 * @param list 集合点
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
				System.out.println("规划有问题");
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
			System.out.println("规划有问题");
		}
		return null;
	}
	
	/**
	 * 获取结果集的全部距离
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
	 * 通过fid来获取坐标
	 * @param fid 
	 * @param gids  楼层组
	 * @param searchAnalyser  搜索器
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
	 * 通过fid获取搜索结果集
	 * @param fid
	 * @param gids  楼层组
	 * @param searchAnalyser 搜索器
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
     * 通过fid查找楼层信息
     * @param fid
     * @param infos  楼层组
     * @param searchAnalyser  搜索器
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
	 * 关键字模糊查询
	 * @param key  关键字
	 * @param gids  楼层组
	 * @param searchAnalyser  搜索器
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
	 * 类型查询
	 * @param type  类型码
	 * @param gids  楼层组
	 * @param searchAnalyser  楼层组
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
			System.out.println("排序前: "+r1.get("no").toString()+"   排序后： "+r2.get("no").toString());
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
