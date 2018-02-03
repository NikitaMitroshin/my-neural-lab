package com.example.getstarted.util;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

@WebListener
public class MyServletContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(HelloAppEngineServlet.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ClassLoader classLoader = getClass().getClassLoader();
        try (final InputStream channelInputStream = classLoader.getResourceAsStream("model.zip")) {
            final ComputationGraph computationGraph = ModelSerializer.restoreComputationGraph(channelInputStream);
            computationGraph.init();
            sce.getServletContext().setAttribute("computationGraph", computationGraph);
        } catch (IOException e) {
            LOGGER.warning(e + " IOException");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ComputationGraph computationGraph = (ComputationGraph) sce.getServletContext().getAttribute("computationGraph");
        if (computationGraph != null) {
            computationGraph.clear();
        }
    }
}
