module com.pld.agile {
    requires javafx.controls;
    requires dom4j;
    exports com.pld.agile.controller;
    exports com.pld.agile.model.map;
    exports com.pld.agile.model.tour;
    exports com.pld.agile.utils.observer;
    exports com.pld.agile.utils.parsing;
    exports com.pld.agile.utils.tsp;
    exports com.pld.agile.utils.view;
    exports com.pld.agile.view;

}