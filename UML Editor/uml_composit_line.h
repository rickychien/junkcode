#ifndef UML_COMPOSIT_LINE_H
#define UML_COMPOSIT_LINE_H

#include <QtGui/QPen>
#include <QtGui/QPainter>
#include "uml_link.h"

class UMLCompositionLine : public UMLLink {
public:
	UMLCompositionLine();
	UMLCompositionLine( UMLObjectPort * startPort, UMLObjectPort * endPort );
	virtual QRectF boundingRect() const;
	virtual void paint( QPainter * painter, 
						const QStyleOptionGraphicsItem * option, QWidget * parent = 0 );

private:
	QPainterPath  _path;
};

#endif
