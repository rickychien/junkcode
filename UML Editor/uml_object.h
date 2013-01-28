#ifndef UML_OBJECT_H
#define UML_OBJECT_H

#include <QtGui/QAbstractGraphicsShapeItem>
#include <QtGui/QPainter>
#include <QtGui/QGraphicsScene>
#include "uml_object_port.h"

class UMLObject : public QAbstractGraphicsShapeItem {
public:
	enum Type {
		Class,
		UseCase
	};

	const qreal PORT_SIZE;

	UMLObject();
	virtual QString getName() = 0;
	virtual void setName( QString name ) = 0;
	virtual QRectF boundingRect() const = 0;
	virtual void paint( QPainter * painter, 
						const QStyleOptionGraphicsItem * option, QWidget * widget = 0 ) = 0;

	UMLObjectPort * getSelectedPort( const QPointF & scenePosition );

protected:
	UMLObjectPort _topPort;
	UMLObjectPort _bottomPort;
	UMLObjectPort _leftPort;
	UMLObjectPort _rightPort;
};

#endif
