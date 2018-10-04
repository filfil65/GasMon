//package org.jzy3d.demos.surface;

import java.util.Random;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

//public class DrawGraph extends AbstractAnalysis {
//    public static void main(String[] args) throws Exception {
//        AnalysisLauncher.open(new DrawGraph());
//    }
//
//    @Override
//    public void init() {
//        // Define a function to plot
//        Mapper mapper = new Mapper() {
//            @Override
//            public double f(double x, double y) {
//                return x * Math.sin(x * y);
//            }
//        };
//
//        // Define range and precision for the function to plot
//        Range range = new Range(-3, 3);
//        int steps = 80;
//
//        // Create the object to represent the function over the given range.
//        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
//        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
//        surface.setFaceDisplayed(true);
//        surface.setWireframeDisplayed(false);
//
//        // Create a chart
//        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
//        chart.getScene().getGraph().add(surface);
//    }
//}

public class DrawGraph extends AbstractAnalysis{
//	public static void main(String[] args) throws Exception {
//		AnalysisLauncher.open(new DrawGraph());
//	}
	Coord3d[] points;
		
//	public DrawGraph(Coord3d[] leakMap) {
//	}
	
	
	public DrawGraph(Coord3d[] leakMap) {
		this.points = leakMap;
	}


	@Override
    public void init(){
        int size = 1000000;
        float x;
        float y;
        float z;
        float a;
        
//        Coord3d[] points = new Coord3d[size];
        Color[]   colors = new Color[size];
//      points[i] = new Coord3d(x, y, z);
//        points[0] = new Coord3d(1f, 1f, 1f);
//        points[1] = new Coord3d(1000f, 1000f, 20f);
//        colors[0] = new Color(0.001f, 0.001f, 0.001f, 0.25f);
//        colors[1] = new Color(1f, 1f, 1f, 0.5f);

        
        Random r = new Random();
        r.setSeed(0);
        
        for(int i=0; i<size; i++){
            x = r.nextFloat() - 0.5f;
            y = r.nextFloat() - 0.5f;
            z = 0.01f;
//            points[i] = new Coord3d(x*1000, y*1000, z*1000);
            a = 0.25f;
            colors[i] = new Color(0.1f, 0.2f, 0.3f, 0.05f);
        }
        
        Scatter scatter = new Scatter(points, colors);
        chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        chart.getScene().add(scatter);
    }
}
