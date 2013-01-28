#ifndef UML_ASSOC_LINE_H
#define UML_ASSOC_LINE_H

#include <QtGui/QPen>
#include <QtGui/QPainter>
#include "uml_link.h"

class UMLAssociationLine : public UMLLink {
public:
	UMLAssociationLine();
	UMLAssociationLine( UMLObjectPort * startPort, UMLObjectPort * endPort );

};

#endif
