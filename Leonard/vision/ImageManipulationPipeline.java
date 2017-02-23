package vision;

import org.opencv.core.Mat;
import vision.capture.MatFrameListener;
import vision.capture.VideoCapture;
import vision.classification.PatternMatcher;
import vision.detection.ImageManipulator;
import vision.detection.manipulators.*;

import java.util.LinkedList;

/**
 * Created by Ivan Georgiev (s1410984) on 01/02/17.
 * A class to orchestrate
 */
public class ImageManipulationPipeline implements MatFrameListener {

    private static final ImageManipulationPipeline instance = new ImageManipulationPipeline();

    public synchronized static ImageManipulationPipeline getInstance() { return instance; }


    private ImageManipulationPipeline() {
        // Initiate Pipeline
        for (int i = 0; i < pipeline.size() - 1; i++) {
            pipeline.get(i).setNext(pipeline.get(i + 1));
        }
        pipeline.getLast().setNext(this);
    }

//    VideoFileCapture                       videoFileCap   = new VideoFileCapture("vision/calibration/pre_saved_values/capture.mkv");
    public VideoCapture                    videoCapture   = new VideoCapture();
    public UndistortImage                  undistortImage = new UndistortImage();
    public HSVConverter                    hsvImage       = new HSVConverter();
    private BackgroundSubtractionThreshold threshold      = new BackgroundSubtractionThreshold();
    public GaussianBlurImage              gaussianBlur   = new GaussianBlurImage();
    public DilateImage                     dilateImage    = new DilateImage();
    private ErodeImage                     erodeImage     = new ErodeImage();
    private ApplyBinaryMask                applyBinaryMask= new ApplyBinaryMask(undistortImage);
    public PatternMatcher                  classifier     = new PatternMatcher(gaussianBlur);

    // Failures

    // Performance heavy
//    private RemoveSmallBlobs               rmSmallBlobs   = new RemoveSmallBlobs();
//    public NonLocalMeansDenoising          denoising      = new NonLocalMeansDenoising();

    // Multiple blurs deemed unnecessary
//    private GaussianBlurImage              gaussianBlur2  = new GaussianBlurImage();

    // Normalized RGB makes a noisy subtraction, which is difficult to threshold
//    NormalizeImage                       normalizeImage = new NormalizeImage()
// ;
    /**
     * This list is what determines what order (and which manipulations are applied to the input
     * video.
     */
    public LinkedList<ImageManipulator> pipeline = new LinkedList<ImageManipulator>() {{
        add(videoCapture);
        add(undistortImage);
        add(gaussianBlur);
        add(hsvImage);
        add(threshold);
        add(erodeImage);
        add(dilateImage);
        add(applyBinaryMask);
        add(classifier);
    }};

    @Override
    public void onFrameReceived(Mat image) {
        // TODO Analyse processed image
    }
}
