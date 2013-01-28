#ifndef UML_GENERAL_LINE_H
#define UML_GENERAL_LINE_H

#include <QtGui/QPen>
#include <QtGui/QPainter>
#include <cmath>
#include "uml_link.h"

class UMLGeneralizationLine : public UMLLink {
public:
	UMLGeneralizationLine();
	UMLGeneralizationLine( UMLObjectPort * startPort, UMLObjectPort * endPort );
	virtual QRectF boundingRect() const;
	virtual void paint( QPainter * painter, 
						const QStyleOptionGraphicsItem * option, QWidget * parent = 0 );
};

#endif
