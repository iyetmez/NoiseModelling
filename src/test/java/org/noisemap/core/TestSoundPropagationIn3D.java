package org.noisemap.core;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.chain.MonotoneChain;
import com.vividsolutions.jts.index.chain.MonotoneChainBuilder;
import com.vividsolutions.jts.index.chain.MonotoneChainSelectAction;
import com.vividsolutions.jts.index.chain.MonotoneChainOverlapAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Iterator;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


import junit.framework.TestCase;

/**
 *
 * @author SU Qi
 */
public class TestSoundPropagationIn3D extends TestCase {


    public void test() throws LayerDelaunayError{
    
           GeometryFactory factory = new GeometryFactory();
           Coordinate[] building1Coords = { new Coordinate(15., 5.,0.),
				new Coordinate(30., 5.,0.), new Coordinate(30., 30.,0.),
				new Coordinate(15., 30.,0.), new Coordinate(15., 5.,0.) };
           Coordinate[] building2Coords = { new Coordinate(40., 5.,0.),
				new Coordinate(45., 5.,0.), new Coordinate(45., 30.,0.),
				new Coordinate(40., 30.,0.), new Coordinate(40., 5.,0.) };
           Polygon building1 = factory.createPolygon(
			factory.createLinearRing(building1Coords), null);
           Polygon building2 = factory.createPolygon(
			factory.createLinearRing(building2Coords), null);     
           FastObstructionTest ft= new FastObstructionTest();
           //add building with height
           ft.addGeometry(building1,5.);
           ft.addGeometry(building2,4.);
           ft.finishPolygonFeeding(new Envelope(new Coordinate(0., 0.,0.), new Coordinate(60., 60.,0.)));
                      
           assertTrue("Intersection test isFreeField #1 failed",ft.isFreeField(new Coordinate(10,5), new Coordinate(12,45)));
           assertFalse("Intersection test isFreeField #2 failed",ft.isFreeField(new Coordinate(10,5), new Coordinate(32,15)));
           assertFalse("Intersection test isFreeField #2 failed",ft.isFreeField(new Coordinate(10,5,6.0), new Coordinate(32,15,7.0)));
           System.out.println("----------------TEST intersection between source and receiver----------------");
           
           System.out.println("----------TEST with 1 building----- ");
           ft.setTriBuildingList(new Coordinate(10,5), new Coordinate(32,15));
           LinkedList<Coordinate[]> lt=ft.getTriBuildingCoordinate();
           
           for(int i=0 ; i<lt.size();i++){
               System.out.println("Triangle "+ (i+1));
               System.out.println(lt.get(i)[0]+ "--" + lt.get(i)[1] + "--" + lt.get(i)[2]);
               System.out.println((ft.getTriBuildingHeight()).get(i));
              
           }
           
           System.out.println("------------------Test intersection---------------");
           
           LinkedList<Coordinate> pointsIntersection=ft.getIntersection();
           for(Coordinate point:pointsIntersection){
               System.out.println(point.toString());
           
           }
           
           System.out.println("----------TEST with 1 buildings other side----- ");
           ft.setTriBuildingList(new Coordinate(32,15,0.5), new Coordinate(47,15,1.0));
           
           lt=ft.getTriBuildingCoordinate();
           
           for(int i=0 ; i<lt.size();i++){
               System.out.println("Triangle "+ (i+1));
               System.out.println(lt.get(i)[0]+ "--" + lt.get(i)[1] + "--" + lt.get(i)[2]);
               System.out.println((ft.getTriBuildingHeight()).get(i));
              
           }
           pointsIntersection=ft.getIntersection();
           for(Coordinate point:pointsIntersection){
               System.out.println(point.toString());
           
           }
           
           System.out.println("----------TEST with special points ----- ");
           ft.setTriBuildingList(new Coordinate(1,16,0.5), new Coordinate(17,32,1.0));
           
           lt=ft.getTriBuildingCoordinate();
           
           for(int i=0 ; i<lt.size();i++){
               System.out.println("Triangle "+ (i+1));
               System.out.println(lt.get(i)[0]+ "--" + lt.get(i)[1] + "--" + lt.get(i)[2]);
               System.out.println("Height:"+(ft.getTriBuildingHeight()).get(i));
              
           }
           System.out.println("------------------Test intersection---------------");
           pointsIntersection=ft.getIntersection();
           for(Coordinate point:pointsIntersection){
               System.out.println(point.toString());
           }
           
           
           System.out.println("----------TEST with 2 buildings----- ");
           ft.setTriBuildingList(new Coordinate(5,15), new Coordinate(48,25));
           
           lt=ft.getTriBuildingCoordinate();
           
           for(int i=0 ; i<lt.size();i++){
               System.out.println("Triangle "+ (i+1));
               System.out.println(lt.get(i)[0]+ "--" + lt.get(i)[1] + "--" + lt.get(i)[2]);
               System.out.println("Height:"+(ft.getTriBuildingHeight()).get(i));
              
           }
           System.out.println("------------------Test intersection---------------");
           pointsIntersection=ft.getIntersection();
           for(Coordinate point:pointsIntersection){
               System.out.println(point.toString());
           }

           
           
           System.out.println("----------------TEST Finished----------------");
         /*
           LineSegment a=new LineSegment(); 
           ft.setListofIntersection();
           ft.getListofIntersection(new Coordinate(10,5), new Coordinate(32,15));
         
           */
           System.out.println("--------------------TEST Javis March----------------------");
           
           
           
           double x[]=new double[100];
           double y[]=new double[100];
           for(int i=0;i<10;i++){
               
               x[i]=(double)i;
               if(i<5)
               {
               y[i]=(double)i;
               }
               else{
               y[i]=(double)i-3;
               }
           
           }
           
           JarvisMarch jm=new JarvisMarch(new JarvisMarch.Points(x,y));
           JarvisMarch.Points points=jm.calculateHull();
           for (int i=0;i<points.x.length;i++){
           System.out.println(points.x[i]+","+points.y[i]);
               System.out.println("---");

             }
           
           System.out.println("--------Test Jarvis Finished--------------");
           System.out.println("--------Test Monotone Chain---------------");
           Coordinate[] coorPt=new Coordinate[10];
           
           for(int i=0;i<10;i++){
                
               if(i>=5){
                coorPt[i]=new Coordinate((double)i,(double)i-4,0.0 );
               
               }
               else{
                coorPt[i]=new Coordinate((double)i,(double)i,0.0 );
               }
              
             
           }
           List<MonotoneChain> a=MonotoneChainBuilder.getChains(coorPt);
  
           System.out.println("--------Test Monotone Chain  Finished---------------");
           }
}