package frc.robot;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * GripPipeline class.
 *
 * <p>An OpenCV pipeline generated by GRIP.
 *
 * @author GRIP
 */
public class GripPipeline {


    /**
     * This is the primary method that runs the entire pipeline and updates the outputs.
     */
    public List<MatOfPoint> process(Mat source) {
        Mat hslOut = new Mat();
        double[] hslThresholdHue = {0., 180.};
        double[] hslThresholdSaturation = {0., 255.0};
        double[] hslThresholdLuminance = {127., 255.0};
        hslThreshold(source, hslThresholdHue, hslThresholdSaturation, hslThresholdLuminance, hslOut);

        Mat rgbOut = new Mat();
        double[] rgbThresholdRed = {0.0, 127.0};
        double[] rgbThresholdGreen = {127.0, 255.0};
        double[] rgbThresholdBlue = {127.0, 255.0};
        rgbThreshold(source, rgbThresholdRed, rgbThresholdGreen, rgbThresholdBlue, rgbOut);

        Mat orOur = new Mat();
        cvBitwiseOr(hslOut, rgbOut, orOur);

        ArrayList<MatOfPoint> output = new ArrayList<>();
        boolean findContoursExternalOnly = false;
        findContours(orOur, findContoursExternalOnly, output);

        return output;
    }

    private void hslThreshold(Mat input, double[] hue, double[] sat, double[] lum,
                              Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
        Core.inRange(out, new Scalar(hue[0], lum[0], sat[0]),
                new Scalar(hue[1], lum[1], sat[1]), out);
    }

    private void rgbThreshold(Mat input, double[] red, double[] green, double[] blue,
                              Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2RGB);
        Core.inRange(out, new Scalar(red[0], green[0], blue[0]),
                new Scalar(red[1], green[1], blue[1]), out);
    }

    private void cvBitwiseOr(Mat src1, Mat src2, Mat dst) {
        Core.bitwise_or(src1, src2, dst);
    }

    private void findContours(Mat input, boolean externalOnly,
                              List<MatOfPoint> contours) {
        Mat hierarchy = new Mat();
        contours.clear();
        int mode;
        if (externalOnly) {
            mode = Imgproc.RETR_EXTERNAL;
        }
        else {
            mode = Imgproc.RETR_LIST;
        }
        int method = Imgproc.CHAIN_APPROX_SIMPLE;
        Imgproc.findContours(input, contours, hierarchy, mode, method);
    }


}

