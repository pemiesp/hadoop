/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doplomadobd.retailda;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.math.plot.Plot2DPanel;
import org.math.plot.plotObjects.BaseLabel;

/**
 *
 * @author mario
 */
public class SplineConverter {
    //Original data

    double[] x = {0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52, 56, 60, 64, 68, 72, 76, 80, 84, 88, 92, 96, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140};
    double[] y = {73600, 81600, 102000, 96600, 113050, 136850, 88750, 95800, 153600, 187700, 187600, 82400, 49100, 83150, 125700, 106450, 170600, 66400, 73500, 132500, 182000, 196500, 133000, 145000, 33000, 84000, 135500, 139000, 104500, 107000, 46500, 65500, 130500, 116000, 195500, 178000};
    UnivariateInterpolator interpolator = new SplineInterpolator();
    Plot2DPanel plot = new Plot2DPanel();

    public SplineConverter() {
        UnivariateFunction polinomio = interpolator.interpolate(x, y);
        double [] xc = new double[141];
        double [] yc = new double [141];
        for (int i = 0; i < xc.length; i++) {
            xc[i]=i;
            yc[i]= polinomio.value(xc[i]);            
        }
        plot.addLegend("SKU NL003");
        plot.addScatterPlot("NL003 Originales Puntos", x,y);
        plot.addLinePlot("NL003 Originales Línea", x,y);
        
        plot.addScatterPlot("NL003 Interpolada Puntos", xc,yc);
        plot.addLinePlot("NL003 Interpolada Línea", xc,yc);
        BaseLabel baseLabel = new BaseLabel("Interpolación del SKU NL003", Color.blue, 0.5, 1.1);
        plot.addPlotable(baseLabel);
         plot.addLegend("SOUTH");

        JFrame frame = new JFrame("Interpolación del SKU NL003");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        frame.add(plot,BorderLayout.CENTER);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new SplineConverter();
    }
}
