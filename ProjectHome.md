Arid POJOs is a framework for simplifying POJO application development.

# Automatic Spring Bean Generation #
Arid POJOs provides a custom Spring/XML namespace that scans a package hierarchy and generate bean definitions for the classes or interfaces that it finds. The classes and interfaces can be filtered by an AspectJ type pattern, which supports various criteria including name matching, annotations, and subtypes. The generated beans use autowiring. Here is a simple example.

```
<beans>
...
    <arid:define-beans
       package='net.chrisrichardson.arid.example.dao'
      autowire='byType'
    /> 
...
</beans>
```

The generated bean definitions can also instantiate an entirely different class. The original interface/class is passed as a constructor argument. This is used, for example, with the dynamic Generic DAO feature.

# Dynamic Generic DAOs #
Arid POJOs can also generate DAO implementations from an interface using a Grails GORM/Rails Active Record like mechanism. Here is an example interface:
```
public interface CustomerRepository extends GenericDao<Customer, Integer> {
	Customer findByCustomerId(String customerId);
	Customer findUsingSomeStrangeNamedQuery(String firstName, String lastName);
}
```
Arid POJOs will generate a proxy, which implements this interface. The findByCustomerId() method is turned into Criteria Query, which searches on the customerId property, and the findUsingSomeStrangeNamedQuery() method ends up calling a named query. This is accomplished using the following bean definitions:
```
<beans>
...
<bean name='parentGenericDaoFactoryBean' abstract='true'
   class='net.chrisrichardson.arid.dao.hibernate.GenericDAOFactoryBean'>
   <property name='sessionFactory' ref='sessionFactory'/>
</bean>


<arid:define-beans package='net.chrisrichardson.arid.dao.exampledomain'
  package-scanner='net.chrisrichardson.arid.InterfaceAndAbstractClassPackageScanner'
  pattern='net.chrisrichardson.arid.domain.GenericDao+'
  bean-generator='net.chrisrichardson.arid.ChildWithConstructorArgBeanGenerator'
  parent-bean-name='parentGenericDaoFactoryBean'>
</arid:define-beans>
...
</beans>
```

