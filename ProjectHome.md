Although regular expressions are an efficient way of describing a language, sometimes they can get a bit complex and start to loose readability.

Python provides a way to document regular expression patterns within themselves. This can be achieved in Java setting the COMMENTS flag on matching process. Although this helps a lot, you still need the comments by hand to reflect the intention of each pattern.

pattern-builder takes a different approach allowing the programmer to specify the patterns explicitly in a functional manner. Here's an example of this:

```
  // "[a-zA-Z]+"
  Pattern onlyLettersPattern =
    characterClass(
      anyInRange("a", "z")
      .orAnyInRange("A", "Z")
    ).oneOrMoreTimes()
    .compile();
```

With this approach, the programmer doesn't need to remember what each pattern did, and can rely on today's modern IDEs auto-completion features.

The interface is inspired in [guava-libraries](http://code.google.com/p/guava-libraries/) and [google-collections](http://code.google.com/p/google-collections/).