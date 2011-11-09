/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.coode.owlapi.rdf.rdfxml;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.coode.xml.IllegalElementNameException;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AbstractOWLOntologyStorer;
import org.semanticweb.owlapi.util.NamespaceUtil;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 03-Jan-2007<br><br>
 */
public class RDFXMLOntologyStorer extends AbstractOWLOntologyStorer {

    public boolean canStoreOntology(OWLOntologyFormat ontologyFormat) {
        return ontologyFormat instanceof RDFXMLOntologyFormat;
    }


    @Override
    protected void storeOntology(OWLOntologyManager manager, OWLOntology ontology, Writer writer, OWLOntologyFormat format) throws OWLOntologyStorageException {
        try {
            RDFXMLRenderer renderer = new RDFXMLRenderer(manager, ontology, writer, format);
            Set<OWLEntity> entities = renderer.getUnserialisableEntities();
            if (!entities.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (OWLEntity entity : entities) {
                    sb.append(entity.toStringID());
                    sb.append("\n");
                }
                throw new OWLOntologyStorageException(sb.toString().trim());
            }
            renderer.render();
        }
        catch (IOException e) {
            throw new OWLOntologyStorageException(e);
        }
        catch (IllegalElementNameException e) {
            throw new OWLOntologyStorageException(e);
        }
    }

//    public Set<UnstorableAxiom> getUnstorableAxioms(OWLOntology ontology) {
//        Set<OWLProperty> processedProperties = new HashSet<OWLProperty>();
//        Set<OWLProperty> unstorableProperties = new HashSet<OWLProperty>();
//        Set<OWLAxiom> unstorableAxioms = new HashSet<OWLAxiom>();
//        for (OWLObjectPropertyAssertionAxiom ax : ontology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
//            processPropertyAssertionAxiom(processedProperties, unstorableProperties, unstorableAxioms, ax);
//        }
//    }
//
//    private void processPropertyAssertionAxiom(Set<OWLProperty> processedProperties, Set<OWLProperty> unstorableProperties, Set<OWLAxiom> unstorableAxioms, OWLPropertyAssertionAxiom<?, ?> ax) {
//        OWLProperty namedProperty = ax.getProperty().getNamedProperty();
//        if (!processedProperties.contains(namedProperty)) {
//            processedProperties.add(namedProperty);
//            if(!canCreateQName(namedProperty)) {
//                unstorableProperties.add(namedProperty);
//            }
//        }
//        if (unstorableProperties.contains(namedProperty)) {
//            unstorableAxioms.add(ax);
//        }
//    }
//
//
//    public boolean canCreateQName(OWLEntity entity) {
//        NamespaceUtil util = new NamespaceUtil();
//        String[] splitResult = new String[2];
//        util.split(entity.toStringID(), splitResult);
//        return !"".equals(splitResult[0]) && !"".equals(splitResult[1]);
//
//    }
}
