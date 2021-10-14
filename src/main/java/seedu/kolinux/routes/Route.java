package seedu.kolinux.routes;

import seedu.kolinux.Main;
import seedu.kolinux.exceptions.KolinuxException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class Route {
    private String[] splitInput;
    private String[] location;
    private int[] vertexCodeAOne;
    private int[] vertexCodeDOne;
    private int[] vertexCodeDTwo;
    private int[] vertexCodeE;
    private int[] vertexCodeK;
    private Graph graphAOne;
    private Graph graphDOne;
    private Graph graphDTwo;
    private Graph graphE;
    private Graph graphK;
    private ArrayList<String> verticesAOne;
    private ArrayList<String> verticesDOne;
    private ArrayList<String> verticesDTwo;
    private ArrayList<String> verticesE;
    private ArrayList<String> verticesK;

    public Route(String input) {
        location = new String[2];
        splitInput = input.split(" /");
        vertexCodeAOne = new int[2];
        vertexCodeDOne = new int[2];
        vertexCodeDTwo = new int[2];
        vertexCodeE = new int[2];
        vertexCodeK = new int[2];
        graphAOne = new Graph(13);
        graphDOne = new Graph(13);
        graphDTwo = new Graph(12);
        graphE = new Graph(7);
        graphK = new Graph(16);
        verticesAOne = new ArrayList<>();
        verticesDOne = new ArrayList<>();
        verticesDTwo = new ArrayList<>();
        verticesE = new ArrayList<>();
        verticesK = new ArrayList<>();
    }

    /**
     * Reads the contents from the file which consists of the path
     * of the graph.
     *
     * @param vertices contains the nodes which connect the graph
     * @param filePath the path of the input file
     * @throws KolinuxException if the user command is not in the correct format
     * @throws IOException if the there any IO errors
     */
    private void readNodesFromFile(ArrayList<String> vertices, String filePath) throws KolinuxException, IOException {
        try {
            InputStream inputStream = Main.class.getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new KolinuxException("File not found");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                vertices.add(line);
            }
        } catch (IOException e) {
            throw new IOException();
        }
    }

    /**
     * Forms the bus route. This is done in the form of an unweighted graph.
     *
     * @param vertices contains the nodes which connect the graph
     * @param graph is the graph which forms the bus route
     * @throws KolinuxException if the user command is not in the correct format
     */
    private void setRoute(ArrayList<String> vertices, Graph graph) throws KolinuxException {
        if (vertices == null) {
            throw new KolinuxException("Route doesn't exist");
        }
        for (String v : vertices) {
            String[] vertex = v.split(" ");
            graph.addEdge(Integer.parseInt(vertex[0]), Integer.parseInt((vertex[1])));
        }
    }

    /**
     * Converts the bus stop locations into bus stop numbers.
     *
     * @throws KolinuxException if the user command is not in the correct format
     */
    private void getLocations() throws KolinuxException {
        for (int i = 0; i < 2; i++) {
            if (splitInput.length < 3) {
                throw new KolinuxException("Enter starting point and final destination");
            }
            location[i] = splitInput[i + 1];
            vertexCodeAOne[i] = getStopNumberAOne(location[i]);
            vertexCodeDOne[i] = getStopNumberDOne(location[i]);
            vertexCodeDTwo[i] = getStopNumberDTwo(location[i]);
            vertexCodeE[i] = getStopNumberE(location[i]);
            vertexCodeK[i] = getStopNumberK(location[i]);
            if (vertexCodeAOne[i] < 0 && vertexCodeDOne[i] < 0
                    && vertexCodeE[i] < 0 && vertexCodeDTwo[i] < 0 && vertexCodeK[i] < 0) {
                throw new KolinuxException(location[i].trim() + " is not a valid bus stop name");
            }
        }
    }

    /**
     * Finds if there are bus routes between the starting and end locations.
     *
     * @return Message which specifies if any routes are found
     * @throws KolinuxException if the user command is not in the correct format
     * @throws IOException if the there any IO errors
     */
    public String checkRoutes() throws KolinuxException, IOException {
        String[] filePaths = {"/routeA1.txt", "/routeD1.txt", "/routeD2.txt", "/routeE.txt", "/routeK.txt"};
        readNodesFromFile(verticesAOne, filePaths[0]);
        readNodesFromFile(verticesDOne, filePaths[1]);
        readNodesFromFile(verticesDTwo, filePaths[2]);
        readNodesFromFile(verticesE, filePaths[3]);
        readNodesFromFile(verticesK, filePaths[4]);
        setRoute(verticesAOne, graphAOne);
        setRoute(verticesDOne, graphDOne);
        setRoute(verticesDTwo, graphDTwo);
        setRoute(verticesE, graphE);
        setRoute(verticesK, graphK);
        getLocations();
        String startLocation = location[0].trim().toUpperCase();
        String endLocation = location[1].trim().toUpperCase();
        ArrayList<String> busNumbers = new ArrayList<>();
        boolean[] flag = new boolean[2];
        flag[0] = checkDirectRoutes(busNumbers);
        if (!flag[0]) {
            ArrayList<String> busNumberOne = new ArrayList<>();
            ArrayList<String> busNumberTwo = new ArrayList<>();
            ArrayList<String> midLocation = new ArrayList<>();
            flag[1] = checkIndirectRoutes(busNumberOne, busNumberTwo, midLocation);
            if (!flag[1]) {
                return "There are no viable bus services from " + startLocation + " to " + endLocation;
            }
            return "Take bus " + busNumberOne + " then change to bus " + busNumberTwo + " at " + midLocation.get(0);
        }
        return "Bus " + busNumbers + " goes from " + startLocation + " to " + endLocation;
    }

    /**
     * Checks if 2 vertices are directly connected by the same route.
     *
     * @param busNumbers buses of the connected bus stops
     * @return true if connected, false otherwise
     */
    private boolean checkDirectRoutes(ArrayList<String> busNumbers) {
        boolean flag = false;
        if (graphAOne.isConnected(vertexCodeAOne[0], vertexCodeAOne[1])) {
            busNumbers.add("A1");
            flag = true;
        }
        if (graphDOne.isConnected(vertexCodeDOne[0], vertexCodeDOne[1])) {
            busNumbers.add("D1");
            flag = true;
        }
        if (graphDTwo.isConnected(vertexCodeDTwo[0], vertexCodeDTwo[1])) {
            busNumbers.add("D2");
            flag = true;
        }
        if (graphE.isConnected(vertexCodeE[0], vertexCodeE[1])) {
            busNumbers.add("E");
            flag = true;
        }
        if (graphK.isConnected(vertexCodeK[0], vertexCodeK[1])) {
            busNumbers.add("K");
            flag = true;
        }
        return flag;
    }

    /**
     * Checks if any 2 vertices are connected by an indirect path which
     * requires a change of bus.
     *
     * @param busOne bus number which connects to the intermediate bus stop
     * @param busTwo bus number which connects from the intermediate bus stop
     *                     to the final location
     * @param midLoc is the intermediate bus stop
     * @return true if connected, false otherwise
     */
    private boolean checkIndirectRoutes(ArrayList<String> busOne, ArrayList<String> busTwo, ArrayList<String> midLoc) {
        boolean flag = false;
        if (vertexCodeAOne[0] > 0) {
            flag = checkIndirectAOne(busOne, busTwo, midLoc);
        } else if (vertexCodeDOne[0] > 0) {
            if (graphDOne.isConnected(vertexCodeDOne[0], getStopNumberDOne("UTOWN"))) {
                flag = checkIndirectDOne(busOne, busTwo, midLoc);
            }
        } else if (vertexCodeDTwo[0] > 0) {
            if (graphDTwo.isConnected(vertexCodeDTwo[0], getStopNumberDTwo("UTOWN"))) {
                flag = checkIndirectDTwo(busOne, busTwo, midLoc);
            }
        } else if (vertexCodeE[0] > 0) {
            flag = checkIndirectE(busOne, busTwo, midLoc);
        } else if (vertexCodeK[0] > 0) {
            if (graphK.isConnected(vertexCodeK[0], getStopNumberK("KENT VALE"))) {
                flag = checkIndirectK(busOne, busTwo, midLoc);
            }
        }
        return flag;
    }

    /**
     * Checks for indirect route in bus route A1.
     *
     * @param busOne bus number which connects to the intermediate bus stop
     * @param busTwo bus number which connects from the intermediate bus stop
     *                     to the final location
     * @param midLoc is the intermediate bus stop
     * @return true if connected, false otherwise
     */
    private boolean checkIndirectAOne(ArrayList<String> busOne, ArrayList<String> busTwo, ArrayList<String> midLoc) {
        boolean flag = false;
        busOne.add("A1");
        midLoc.add("PGP");
        if (graphDTwo.isConnected(getStopNumberDTwo("PGP"), vertexCodeDTwo[1])) {
            busTwo.add("D2");
            flag = true;
        }
        if (graphK.isConnected(getStopNumberK("PGP"), vertexCodeK[1])) {
            busTwo.add("K");
            flag = true;
        }
        return flag;
    }

    /**
     * Checks for indirect route in bus route D1.
     *
     * @param busOne bus number which connects to the intermediate bus stop
     * @param busTwo bus number which connects from the intermediate bus stop
     *                     to the final location
     * @param midLoc is the intermediate bus stop
     * @return true if connected, false otherwise
     */
    private boolean checkIndirectDOne(ArrayList<String> busOne, ArrayList<String> busTwo, ArrayList<String> midLoc) {
        boolean flag = false;
        busOne.add("D1");
        midLoc.add("UTOWN");
        if (graphDTwo.isConnected(getStopNumberDTwo("UTOWN"), vertexCodeDTwo[1])) {
            busTwo.add("D2");
            flag = true;
        }
        if (graphE.isConnected(getStopNumberE("UTOWN"), vertexCodeE[1])) {
            busTwo.add("E");
            flag = true;
        }
        return flag;
    }

    /**
     * Checks for indirect route in bus route D2.
     *
     * @param busOne bus number which connects to the intermediate bus stop
     * @param busTwo bus number which connects from the intermediate bus stop
     *                     to the final location
     * @param midLoc is the intermediate bus stop
     * @return true if connected, false otherwise
     */
    private boolean checkIndirectDTwo(ArrayList<String> busOne, ArrayList<String> busTwo, ArrayList<String> midLoc) {
        boolean flag = false;
        busOne.add("D2");
        midLoc.add("UTOWN");
        if (graphDOne.isConnected(getStopNumberDOne("UTOWN"), vertexCodeDOne[1])) {
            busTwo.add("D1");
            flag = true;
        }
        if (graphE.isConnected(getStopNumberE("UTOWN"), vertexCodeE[1])) {
            busTwo.add("E");
            flag = true;
        }
        return flag;
    }

    /**
     * Checks for indirect route in bus route E.
     *
     * @param busOne bus number which connects to the intermediate bus stop
     * @param busTwo bus number which connects from the intermediate bus stop
     *                     to the final location
     * @param midLoc is the intermediate bus stop
     * @return true if connected, false otherwise
     */
    private boolean checkIndirectE(ArrayList<String> busOne, ArrayList<String> busTwo, ArrayList<String> midLoc) {
        boolean flag = false;
        busOne.add("E");
        if (vertexCodeE[0] == 6) {
            if (graphK.isConnected(getStopNumberK("KENT VALE"), vertexCodeK[1])) {
                midLoc.add("KENT VALE");
                busTwo.add("K");
                flag = true;
            }
        } else {
            midLoc.add("UTOWN");
            if (graphDOne.isConnected(getStopNumberDOne("UTOWN"), vertexCodeDOne[1])) {
                busTwo.add("D1");
                flag = true;
            }
            if (graphDTwo.isConnected(getStopNumberDTwo("UTOWN"), vertexCodeDTwo[1])) {
                busTwo.add("D2");
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Checks for indirect route in bus route K.
     *
     * @param busOne bus number which connects to the intermediate bus stop
     * @param busTwo bus number which connects from the intermediate bus stop
     *                     to the final location
     * @param midLoc is the intermediate bus stop
     * @return true if connected, false otherwise
     */
    private boolean checkIndirectK(ArrayList<String> busOne, ArrayList<String> busTwo, ArrayList<String> midLoc) {
        boolean flag = false;
        busOne.add("K");
        midLoc.add("KENT VALE");
        if (graphE.isConnected(getStopNumberE("KENT VALE"), vertexCodeE[1])) {
            busTwo.add("E");
            flag = true;
        }
        return flag;
    }

    /**
     * Checks the given bus stop name with the corresponding bus stop
     * number in the graph A1 if any.
     *
     * @param command the bus stop name
     * @return the corresponding bus stop number
     */
    private int getStopNumberAOne(String command) {
        assert command != null;
        switch (command.trim().toLowerCase()) {
        case "kr bus terminal":
            return 0;
        case "lt13":
            return 1;
        case "as 5":
            return 2;
        case "com 2":
            return 3;
        case "biz 2":
            return 4;
        case "opp tcoms":
            return 5;
        case "pgp":
            return 6;
        case "kr mrt":
            return 7;
        case "lt27":
            return 8;
        case "uhall":
            return 9;
        case "opp uhc":
            return 10;
        case "yih":
            return 11;
        case "clb":
            return 12;
        default:
            return -1;
        }
    }

    /**
     * Checks the given bus stop name with the corresponding bus stop
     * number in the graph D1 if any.
     *
     * @param command the bus stop name
     * @return the corresponding bus stop number
     */
    public int getStopNumberDOne(String command) {
        assert command != null;
        switch (command.trim().toLowerCase()) {
        case "opp hssml":
            return 0;
        case "opp nuss":
            return 1;
        case "com 2":
            return 2;
        case "ventus":
            return 3;
        case "it":
            return 4;
        case "opp yih":
            return 5;
        case "museum":
            return 6;
        case "utown":
            return 7;
        case "yih":
            return 8;
        case "clb":
            return 9;
        case "lt13":
            return 10;
        case "as 5":
            return 11;
        case "biz 2":
            return 12;
        default:
            return -1;
        }
    }

    /**
     * Checks the given bus stop name with the corresponding bus stop
     * number in the graph D2 if any.
     *
     * @param command the bus stop name
     * @return the corresponding bus stop number
     */
    public int getStopNumberDTwo(String command) {
        assert command != null;
        switch (command.trim().toLowerCase()) {
        case "pgp":
            return 0;
        case "kr mrt":
            return 1;
        case "lt27":
            return 2;
        case "uhall":
            return 3;
        case "opp uhc":
            return 4;
        case "museum":
            return 5;
        case "utown":
            return 6;
        case "uhc":
            return 7;
        case "opp uhall":
            return 8;
        case "s 17":
            return 9;
        case "opp kr mrt":
            return 10;
        case "pgpr":
            return 11;
        default:
            return -1;
        }
    }

    /**
     * Checks the given bus stop name with the corresponding bus stop
     * number in the graph E if any.
     *
     * @param command the bus stop name
     * @return the corresponding bus stop number
     */
    public int getStopNumberE(String command) {
        assert command != null;
        switch (command.trim().toLowerCase()) {
        case "kent vale":
            return 0;
        case "ea":
            return 1;
        case "sd3":
            return 2;
        case "it":
            return 3;
        case "opp yih":
            return 4;
        case "utown":
            return 5;
        case "raffles hall":
            return 6;
        default:
            return -1;
        }
    }

    /**
     * Checks the given bus stop name with the corresponding bus stop
     * number in the graph K if any.
     *
     * @param command the bus stop name
     * @return the corresponding bus stop number
     */
    public int getStopNumberK(String command) {
        assert command != null;
        switch (command.trim().toLowerCase()) {
        case "pgp":
            return 0;
        case "kr mrt":
            return 1;
        case "lt27":
            return 2;
        case "uhall":
            return 3;
        case "opp uhc":
            return 4;
        case "yih":
            return 5;
        case "clb":
            return 6;
        case "opp sde 3":
            return 7;
        case "japanese pri school":
            return 8;
        case "kent vale":
            return 9;
        case "museum":
            return 10;
        case "uhc":
            return 11;
        case "opp uhall":
            return 12;
        case "s 17":
            return 13;
        case "opp kr mrt":
            return 14;
        case "pgpr":
            return 15;
        default:
            return -1;
        }
    }
}
