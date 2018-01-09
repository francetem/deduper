package org.ehu.dedupe.derive.image;

import Jama.LUDecomposition;
import Jama.Matrix;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.shape.Rectangle;
import org.openimaj.math.geometry.shape.Shape;
import org.openimaj.math.geometry.transforms.AffineTransformModel;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;

import java.awt.image.BufferedImage;

public class ImageComparator {

    private final DoGSIFTEngine engine = new DoGSIFTEngine();
    private final ImageUtils imageUtils = new ImageUtils();

    public ImageComparator() {

    }

    public Match levImaJPHash(String inputTarget, String inputQuery) {
        try {
            System.out.println("processing " + inputTarget + " against: " + inputQuery);
            MBFImage target = ImageUtilities.readMBF(imageUtils.getInput(inputTarget));
            MBFImage query = ImageUtilities.readMBF(imageUtils.getInput(inputQuery));

            if (target.getWidth() < query.getWidth() && target.getHeight() < query.getHeight()) {
                MBFImage aux = query;
                query = target;
                target = aux;
            }

            LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(query.flatten());
            LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target.flatten());

            RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(5, 100000, new RANSAC.PercentageInliersStoppingCondition(0.5));

            LocalFeatureMatcher<Keypoint> matcher = new ConsistentLocalFeatureMatcher2d<>(new FastBasicKeypointMatcher<>(8), modelFitter);

            //LocalFeatureMatcher<Keypoint> matcher = new BasicTwoWayMatcher<>();

            matcher.setModelFeatures(queryKeypoints);
            matcher.findMatches(targetKeypoints);

           // MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), RGBColour.RED);

            //DisplayUtilities.display(consistentMatches);

            BufferedImage image2 = ImageUtilities.createBufferedImage(query);
            ImagePHash imagePHash = new ImagePHash();
            String hash2 = imagePHash.getHash(image2);
            AffineTransformModel model = modelFitter.getModel();
            Matrix transform = model.getTransform();
            LUDecomposition luDecomposition = new LUDecomposition(transform);
            if (!luDecomposition.isNonsingular()) {
                return new Match(new PHashedImage(inputTarget, imagePHash.getHash(ImageUtilities.createBufferedImage(target))), new PHashedImage(inputQuery, hash2), false);
            }
            Matrix inverse = transform.inverse();
            Rectangle bounds = query.getBounds();
            Shape shape = bounds.transform(inverse);
            //target.drawShape(shape, 3, RGBColour.BLUE);
            //DisplayUtilities.display(target);

            //query.drawShape(target.getBounds().transform(transform), 3, RGBColour.BLUE);
            //DisplayUtilities.display(query);
            //Thread.sleep(60000);


            Rectangle rectangle = shape.calculateRegularBoundingBox();
            if (rectangle.getHeight() > target.getHeight() * 1.005 || rectangle.getWidth() > target.getWidth() * 1.005) {
                return new Match(new PHashedImage(inputTarget, imagePHash.getHash(ImageUtilities.createBufferedImage(target))), new PHashedImage(inputQuery, hash2), false);
            }
            MBFImage fImages = target.extractROI(rectangle);

            BufferedImage bufferedImage = ImageUtilities.createBufferedImage(fImages);

            String hash1 = imagePHash.getHash(bufferedImage);
            System.out.println(hash1);

            System.out.println(hash2);
            return new Match(new PHashedImage(inputTarget, hash1), new PHashedImage(inputQuery, hash2), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}