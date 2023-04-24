from mpegdash.utils import (
    parse_attr_value, parse_child_nodes, parse_node_value,
    write_attr_value, write_child_node, write_node_value
)


class XMLNode(object):
    def parse(self, xmlnode):
        raise NotImplementedError('Should have implemented this')

    def write(self, xmlnode):
        raise NotImplementedError('Should have implemented this')


class Subset(XMLNode):
    def __init__(self):
        self.id = None                                        # xs:string
        self.contains = []                                    # UIntVectorType (required)

    def parse(self, xmlnode):
        self.id = parse_attr_value(xmlnode, 'id', str)
        self.contains = parse_attr_value(xmlnode, 'contains', [int])

    def write(self, xmlnode):
        write_attr_value(xmlnode, 'id', self.id)
        write_attr_value(xmlnode, 'contains', self.contains)


class URL(XMLNode):
    def __init__(self):
        self.source_url = None                                # xs:anyURI
        self.range = None                                     # xs:string

    def parse(self, xmlnode):
        self.source_url = parse_attr_value(xmlnode, 'sourceURL', str)
        self.range = parse_attr_value(xmlnode, 'range', str)

    def write(self, xmlnode):
        write_attr_value(xmlnode, 'sourceURL', self.source_url)
        write_attr_value(xmlnode, 'range', self.range)


class BaseURL(XMLNode):
    def __init__(self):
        self.base_url_value = None                            # xs:anyURI

        self.service_location = None                          # xs:string
        self.byte_range = None                                # xs:string
        self.availability_time_offset = None                  # xs:double
        self.availability_time_complete = None                # xs:boolean
def parse(self, xmlnode):
        self.base_url_value = parse_node_value(xmlnode, str)

        self.service_location = parse_attr_value(xmlnode, 'serviceLocation', str)
        self.byte_range = parse_attr_value(xmlnode, 'byteRange', str)
        self.availability_time_offset = parse_attr_value(xmlnode, 'availabilityTimeOffset', float)
        self.availability_time_complete = parse_attr_value(xmlnode, 'availabilityTimeComplete', bool)

    def write(self, xmlnode):
        write_node_value(xmlnode, self.base_url_value)

        write_attr_value(xmlnode, 'serviceLocation', self.service_location)
        write_attr_value(xmlnode, 'byteRange', self.byte_range)
        write_attr_value(xmlnode, 'availabilityTimeOffset', self.availability_time_offset)
        write_attr_value(xmlnode, 'availabilityTimeComplete', self.availability_time_complete)
class XsStringElement(XMLNode):
    def __init__(self):
        self.text = None

    def parse(self, xmlnode):
        self.text = parse_node_value(xmlnode, str)

    def write(self, xmlnode):
        write_node_value(xmlnode, self.text)


class ProgramInformation(XMLNode):
    def __init__(self):
        self.lang = None                                      # xs:language
        self.more_information_url = None                      # xs:anyURI

        self.titles = None                                    # xs:string*
        self.sources = None                                   # xs:string*
        self.copyrights = None                                # xs:string*

    def parse(self, xmlnode):
        self.lang = parse_attr_value(xmlnode, 'lang', str)
        self.more_information_url = parse_attr_value(xmlnode, 'moreInformationURL', str)

        self.titles = parse_child_nodes(xmlnode, 'Title', XsStringElement)
        self.sources = parse_child_nodes(xmlnode, 'Source', XsStringElement)
        self.copyrights = parse_child_nodes(xmlnode, 'Copyright', XsStringElement)

    def write(self, xmlnode):
        write_attr_value(xmlnode, 'lang', self.lang)
        write_attr_value(xmlnode, 'moreInformationURL', self.more_information_url)

        write_child_node(xmlnode, 'Title', self.titles)
        write_child_node(xmlnode, 'Source', self.sources)
        write_child_node(xmlnode, 'Copyright', self.copyrights)
