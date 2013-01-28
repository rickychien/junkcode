#ifndef UML_OBJECT_GROUPH
#define UML_OBJECT_GROUPH

#include <QGraphicsItemGroup>
#include <QPen>
#include <QPainter>

class UMLObjectGroup : public QGraphicsItemGroup {
public:
	UMLObjectGroup();
	virtual void paint( QPainter * painter,
						const QStyleOptionGraphicsItem * option, QWidget * widget = 0 );
};

#endif