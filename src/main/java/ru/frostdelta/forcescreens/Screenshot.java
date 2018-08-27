package ru.frostdelta.forcescreens;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import ru.frostdelta.forcescreens.network.Action;
import sun.misc.BASE64Encoder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import net.minecraft.client.Minecraft;
import com.google.common.io.ByteStreams;
import com.google.common.io.ByteArrayDataOutput;

public class Screenshot extends Thread {
    private final String clientID;
    private final byte[] imgBytes;

    public Screenshot(String clientID) {
        this.clientID = clientID;

        GL11.glReadBuffer(GL11.GL_FRONT);
        int width = Minecraft.getMinecraft().displayWidth;
        int height = Minecraft.getMinecraft().displayHeight;
        int bpp = 4;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imgBytes = baos.toByteArray();
    }

    @Override
    public void run() {
        try {

            URL obj = new URL("https://api.imgur.com/3/image");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Client-ID " + clientID);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes("image=" + URLEncoder.encode(new BASE64Encoder().encode(imgBytes), "UTF-8"));
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(Action.SCREENSHOT.getActionName());
            out.writeUTF(response.toString().split("\"")[5]);
            Utils.sendPacket(out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
