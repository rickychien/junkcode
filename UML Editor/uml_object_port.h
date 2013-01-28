#ifndef UML_OBJECT_PORT_H
#define UML_OBJECT_PORT_H

#include <QtGui/QGraphicsItem>
#include <QtGui/QPainter>
#include <QList>

class UMLObject;
class UMLLink;

class UMLObjectPort {
public:
	UMLObjectPort();

	QPointF getOffset();
	void setOffset( QPointF offset );
	UMLObject * getParentUMLObj();
	void setParentUMLObj( UMLObject * parentUMLObj );
	void addLink( UMLLink * link );
	void updateLinks();

private:
	QList< UMLLink * > _umlLinkList;
	QPointF _offset;
	UMLObject * _parentUMLObj;

};

#endif
