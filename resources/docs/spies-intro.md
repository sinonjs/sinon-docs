<ul class="nav">
  <li><a href="#sinonspy">sinon.spy()</a></li>
  <li><a href="#spies-api">spy API</a></li>
  <li><a href="#spycall">spy call API</a></li>
</ul>

### What is a test spy?

A test spy is a function that records arguments, return value, the value of
`this` and exception thrown (if any) for all its calls. A test spy can be an
anonymous function or it can wrap an existing function.

### When to use spies?

Test spies are useful to test both callbacks and how certain functions/methods
are used throughout the system under test. The following simplified example
shows how to use spies to test how a function handles a callback:

<pre class="code-snippet" data-lang="javascript"><code>"test should call subscribers on publish": function () {
    var callback = sinon.spy();
    PubSub.subscribe("message", callback);

    PubSub.publishSync("message");

    assertTrue(callback.called);
}</code></pre>

### Spying on existing methods

`sinon.spy` can also spy on existing functions. When doing so, the original
function will behave just as normal (including when used as a constructor) but
you will have access to data about all calls. The following is a slightly
contrived example:

<pre class="code-snippet" data-lang="javascript"><code>{
    setUp: function () {
        sinon.spy(jQuery, "ajax");
    },

    tearDown: function () {
        jQuery.ajax.restore(); // Unwraps the spy
    },

    "test should inspect jQuery.getJSON's usage of jQuery.ajax": function () {
        jQuery.getJSON("/some/resource");

        assert(jQuery.ajax.calledOnce);
        assertEquals("/some/resource", jQuery.ajax.getCall(0).args[0].url);
        assertEquals("json", jQuery.ajax.getCall(0).args[0].dataType);
    }
}</code></pre>

### Creating spies: `sinon.spy()`
<a name="sinonspy"></a>

<dl>
  <dt>`var spy = sinon.spy();`</dt>
  <dd>
    Creates an anonymous function that records arguments, `this` value,
    exceptions and return values for all calls.
  </dd>
  <dt>`var spy = sinon.spy(myFunc);`</dt>
  <dd>Spies on the provided function</dd>
  <dt>`var spy = sinon.spy(object, "method");`</dt>
  <dd>
    Creates a [spy](#spyprops) for `object.method` and replaces the original
    method with the spy. The spy acts exactly like the original method in all
    cases. The original method can be restored by calling
    `object.method.restore()`. The returned spy is the function object which
    replaced the original method. `spy === object.method`.
  </dd>
</dl>

### Spy API
<a name="spyprops"></a>

Spies provide a rich interface to inspect their usage. The above examples showed
the `calledOnce` boolean property as well as the `getCall` method and the
returned object's `args` property. There are three ways of inspecting call data.

The preferred approach is to use the spy's `calledWith` method (and friends)
because it keeps your test from being too specific about which call did what and
so on. It will return `true` if the spy was ever called with the provided
arguments.

<pre class="code-snippet" data-lang="javascript"><code>"test should call subscribers with message as first argument" : function () {
    var message = 'an example message';
    var spy = sinon.spy();

    PubSub.subscribe(message, spy);
    PubSub.publishSync(message, "some payload");

    assert(spy.calledWith(message));
}</code></pre>

If you want to be specific, you can directly check the first argument of the
first call. There are two ways of achieving this:

<pre class="code-snippet" data-lang="javascript"><code>"test should call subscribers with message as first argument" : function () {
    var message = 'an example message';
    var spy = sinon.spy();

    PubSub.subscribe(message, spy);
    PubSub.publishSync(message, "some payload");

    assertEquals(message, spy.args[0][0]);
}</code></pre>

<pre class="code-snippet" data-lang="javascript"><code>"test should call subscribers with message as first argument" : function () {
    var message = 'an example message';
    var spy = sinon.spy();

    PubSub.subscribe(message, spy);
    PubSub.publishSync(message, "some payload");

    assertEquals(message, spy.getCall(0).args[0]);
}</code></pre>

The first example uses the two-dimensional `args` array directly on the spy,
while the second example fetches the first call object and then accesses it's
`args` array. Which one to use is a matter of preference, but the recommended
approach is going with `spy.calledWith(arg1, arg2, ...)` unless there's a need
to make the tests highly specific.
