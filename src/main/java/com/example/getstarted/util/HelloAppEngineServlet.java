package com.example.getstarted.util;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet(name = "Cat or Dog Recognizer", value = "/recognizeCatOrDog")
@MultipartConfig
public class HelloAppEngineServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HelloAppEngineServlet.class.getName());
    private static final String IMAGE_PARAM = "image";
    private static final Double THRESHOLD = 0.95;
    private static final String BASE_PAGE_PATH = "/base.jsp";

    private static ComputationGraph computationGraph;

    @Override
    public void init() throws ServletException {
        super.init();
        computationGraph = (ComputationGraph) getServletContext().getAttribute("computationGraph");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final PrintWriter writer = response.getWriter();
        response.setContentType("text/plain");
        if (computationGraph == null) {
            writer.println("computationGraph is null");
            return;
        }

        try {
            final Part image = request.getPart(IMAGE_PARAM);
            if (image == null) {
                writer.println("image is null");
                return;
            }
            try (final InputStream imageInputStream = image.getInputStream()) {
                final PetType petType = detectCat(computationGraph, imageInputStream);
                request.setAttribute("petType", petType);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(BASE_PAGE_PATH);
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.warning("Exception" + e);
            writer.println("An error happen during recognition process = " + e);
        }
    }

    private static PetType detectCat(final ComputationGraph computationGraph, final InputStream imageInputStream)
            throws IOException {
        LOGGER.log(Level.ALL, "summary =" + computationGraph.summary());
        final NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
        final INDArray image = loader.asMatrix(imageInputStream);
        final DataNormalization scaler = new VGG16ImagePreProcessor();
        scaler.transform(image);
        final INDArray output = computationGraph.outputSingle(false, image);
        if (output.getDouble(0) > THRESHOLD) {
            return PetType.CAT;
        } else if (output.getDouble(1) > THRESHOLD) {
            return PetType.DOG;
        }
        return PetType.NOT_KNOWN;
    }

}
