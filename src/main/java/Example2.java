import com.bordercloud.sparql.SparqlClient;
import com.bordercloud.sparql.SparqlClientException;
import com.bordercloud.sparql.SparqlResult;
import com.bordercloud.sparql.SparqlResultModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class Example2 {

    // https://linkedwiki.com/query/DBpedia_and_Wikidata_Meshup_---_Galaxies
    public static void main(String[] args) {
        try {
            URI endpoint = new URI("https://linkeddata.uriburner.com/sparql");

            //language=SPARQL
            String querySelect =
                    "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
                            "PREFIX dct: <http://purl.org/dc/terms/>\n" +
                            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX p: <http://www.wikidata.org/prop/>\n" +
                            "PREFIX pq: <http://www.wikidata.org/prop/qualifier/>\n" +
                            "PREFIX ps: <http://www.wikidata.org/prop/statement/>\n" +
                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                            "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
                            "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
                            "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
                            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                            "\n"
                            +
                            "SELECT DISTINCT\n"
                            + "        ?dbpediaID\n"
                            + "        (?item  as ?wikidataID)\n"
                            + "        (?label as ?name)\n"
                            + "        ?description\n" +
                            "        ?subjectText\n" +
                            "        ?picture\n"
                            + "WHERE\n" +
                            "{\n"
                            + "    SERVICE <https://query.wikidata.org/sparql>\n"
                            + "    {\n"
                            + "        SELECT DISTINCT ?item ?numero (SAMPLE(?pic) AS ?picture)\n"
                            + "        WHERE {\n"
                            +
                            "                  ?item p:P528 ?catalogStatement .\n"
                            + "                  ?catalogStatement pq:P972 wd:Q14530 .\n"
                            + "                  ?catalogStatement ps:P528 ?numero .\n"
                            + "                  OPTIONAL {?item wdt:P18 ?pic } .\n" +
                            "                  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }\n"
                            + "              }\n" +
                            "        GROUP BY ?item  ?numero ORDER BY ?numero\n"
                            + "        LIMIT 10\n"
                            + "        }\n"
                            + "\n"
                            + "    SERVICE <http://dbpedia.org/sparql>\n"
                            + "    {\n"
                            + "        SELECT ?dbpediaID ?label ?image ?description ?subjectText\n" +
                            "        WHERE {\n" +
                            "                  ?dbpediaID owl:sameAs ?item "
                            + " ;\n" +
                            "                            rdfs:label ?label"
                            + " ;\n" +
                            "#                             foaf:depiction ?imag"
                            + "e;\n" +
                            "                             rdfs:comment ?description"
                            + " ;\n" +
                            "                             dct:subject [ rdfs:label ?subjectText ]"
                            + " .\n" +
                            "                  FILTER (LANG(?label) = \"en"
                            + "\")\n" +
                            "                  FILTER (LANG(?description) = \"en"
                            + "\")\n" +
                            "              "
                            + "}\n" +
                            "      " +
                            "  }\n" +
                            "}\n" +
                            "LIMIT 5";

            SparqlClient sc = new SparqlClient(false);
            sc.setEndpointRead(endpoint);
            SparqlResult sr = sc.query(querySelect);
            //sc.printLastQueryAndResult();

            printResult(sr.getModel(), 30);
        } catch (URISyntaxException | SparqlClientException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static void printResult(SparqlResultModel rs, int size) {
        for (String variable : rs.getVariables()) {
            System.out.print(String.format("%-" + size + "." + size + "s", variable) + " | ");
        }
        System.out.print("\n");
        for (HashMap row : rs.getRows()) {
            for (String variable : rs.getVariables()) {
                System.out.print(String.format("%-" + size + "." + size + "s", row.get(variable)) + " | ");
            }
            System.out.print("\n");
        }
    }
}