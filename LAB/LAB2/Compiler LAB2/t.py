digraph DFA {
    node [shape=circle];

    // 起始状态
    0 -> 1 [label="[0-9]"];
    0 -> 7 [label="[0-9]"];
    0 -> 8 [label="?"];
    0 -> 9 [label=":"];
    0 -> 10 [label="+"];
    0 -> 11 [label="-"];
    0 -> 12 [label="*"];
    0 -> 13 [label="^"];
    0 -> 14 [label="="];
    0 -> 15 [label=">"];
    0 -> 16 [label="<"];
    0 -> 17 [label="&"];
    0 -> 18 [label="|"];
    0 -> 19 [label="!"];
    0 -> 22 [label="("];
    0 -> 23 [label=")"];
    0 -> 26 [label="t"];
    0 -> 29 [label="f"];
    0 -> 34 [label="s"];
    0 -> 36 [label="c"];
    0 -> 40 [label="m"];
    0 -> 45 [label=","];
};
