import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class CompareTwoImages {
	private static BufferedImage image1, image2, imageResult;
	private static boolean isIdentic;
	// a number of pieces along vertical and horizontal
	private static int compareX, compareY;
	
	// “tolerance” comparison parameter that allows to treat similar colors as the same
	private static double sensitivity = 0.10;
	
	public CompareTwoImages(String file1, String file2) throws IOException {
		image1 = loadJPG(file1);
		image2 = loadJPG(file2);
	}
	
	// set a number of pieces along vertical and horizontal
	public void setParameters(int compareX, int compareY) {
		CompareTwoImages.compareX = compareX;
		CompareTwoImages.compareY = compareY;
	}
	
	// compare the two images in this object.
	public void compare() {
		// setup change display image
		imageResult = new BufferedImage(image2.getWidth(null), image2.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = imageResult.createGraphics();
		g2.drawImage(image2, null, null);
		g2.setColor(Color.RED);
		// assign size of each section
		int blocksX = (int)(image1.getWidth()/compareX);
		int blocksY = (int)(image1.getHeight()/compareY);
		CompareTwoImages.isIdentic = true;
		for (int y = 0; y < compareY; y++) {
			for (int x = 0; x < compareX; x++) {
				int result1 [][] = convertTo2D(image1.getSubimage(x*blocksX, y*blocksY, blocksX - 1, blocksY - 1));
				int result2 [][] = convertTo2D(image2.getSubimage(x*blocksX, y*blocksY, blocksX - 1, blocksY - 1));
				for (int i = 0; i < result1.length; i++) {
					for (int j = 0; j < result1[0].length; j++) {
						int diff = Math.abs(result1[i][j] - result2[i][j]);
						if (diff/Math.abs(result1[i][j]) > sensitivity) {
							// draw an indicator on the change image to show where change was detected.
							g2.drawRect(x*blocksX, y*blocksY, blocksX - 1, blocksY - 1);
							isIdentic = false;
						}
					}
				}
			}
		}
	}
	
	public BufferedImage getImageResult() {
		return imageResult;
	}
	
	// representation fragment's of the picture in a two-dimensional integer array
	public int[][] convertTo2D(BufferedImage subimage) {
		int width = subimage.getWidth();
		int height = subimage.getHeight();
		int[][] result = new int[height][width];
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				result[row][col] = subimage.getRGB(col, row);
			}
		}
		return result;
	}

	// reading picture from disk
	public static BufferedImage loadJPG(String filename) throws IOException {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
		
	}
	
	// writing picture into disk
	public static void saveJPG(BufferedImage bimg, String filename) {
		try {
			File outputfile = new File(filename);
			ImageIO.write(bimg, "jpg", outputfile);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isIdentic() {
		return isIdentic;
	}

	
}
