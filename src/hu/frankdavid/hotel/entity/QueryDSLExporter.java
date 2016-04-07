package hu.frankdavid.hotel.entity;

import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.codegen.Keywords;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.beans.Transient;
import java.io.File;

public class QueryDSLExporter {
    public void export() {
        GenericExporter exporter = new GenericExporter();
        exporter.setKeywords(Keywords.JPA);
        exporter.setEntityAnnotation(Entity.class);
        exporter.setEmbeddableAnnotation(Embeddable.class);
        exporter.setEmbeddedAnnotation(Embedded.class);
        exporter.setSupertypeAnnotation(MappedSuperclass.class);
        exporter.setSkipAnnotation(Transient.class);
        exporter.setTargetFolder(new File("src"));
        exporter.setPackageSuffix(".querydsl");
        exporter.export(User.class.getPackage());
    }


    public static void main(String[] args) {
        QueryDSLExporter exporter = new QueryDSLExporter();
        exporter.export();
        System.out.println("export ready");
//        exporter.enhance();
    }

}