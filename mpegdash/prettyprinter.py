import re


def pretty_print(xmlstr, indent='    ', line_break='\n'):
    # python2 doesn't have nonlocal
    current = [0]

    def indent_line(line):
        addition = 0

        if re.match(r'.+</\w[^>]*>$', line):
            # single line text element, don't change indentation
            addition = 0
        elif re.match(r'^</\w', line) and current[0] > 0:
            # end of element and have padding, decrement indentation by one
            current[0] -= 1
        elif re.match(r'^<\w[^>]*[^/]>.*$', line):
            # start of element, increment indentation by one
            addition = 1
        else:
            # single line element, don't change indentation
            addition = 0

        # update and store current indentation in outer function
        current[0] += addition

        # pad the line and return
        return (indent * (current[0] - addition)) + line

    # split the document into lines, indent each line, then rejoin lines
    return line_break.join(
        map(
            indent_line,
            re.sub('(>)(<)(/*)', r'\1\n\2\3', xmlstr).split('\n')
        )
    )
