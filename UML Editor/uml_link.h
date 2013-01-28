#ifndef UML_LINK_H
#define UML_LINK_H

#include <QtGui/QGraphicsLineItem>
#include <QtGui/QPen>
#include <QtGui/QPainter>

class UMLObjectPort;
class UMLObject;

class UMLLink : public QGraphicsLineItem {
public:
	enum Type {
		AssociationType,
		GeneralizationType,
		CompositionType
	};

	UMLLink();
	UMLLink( UMLObjectPort * startPort, UMLObjectPort * endPort );
	
	virtual void drawLink();

protected:
	UMLObjectPort * _startPort;
	UMLObjectPort * _endPort;

};

#endif
