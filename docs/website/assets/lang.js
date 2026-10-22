// Language switch for kq42.de. Both languages are in each page; this sets <html data-lang>.
// The choice is stored in localStorage only when the visitor clicks the switch (never on load),
// so a plain visit stores nothing. Without JavaScript the page stays German.
(function () {
  var KEY = 'kq42-lang';
  var root = document.documentElement;

  function apply(lang) {
    root.setAttribute('data-lang', lang);
    root.lang = lang;
    var title = root.getAttribute('data-title-' + lang);
    if (title) document.title = title;
    var buttons = document.querySelectorAll('[data-set-lang]');
    for (var i = 0; i < buttons.length; i++) {
      buttons[i].setAttribute('aria-pressed', String(buttons[i].getAttribute('data-set-lang') === lang));
    }
  }

  var saved = null;
  try { saved = localStorage.getItem(KEY); } catch (e) { /* storage blocked */ }
  var browser = (navigator.language || 'de').slice(0, 2).toLowerCase() === 'de' ? 'de' : 'en';
  apply(saved === 'de' || saved === 'en' ? saved : browser);

  document.addEventListener('click', function (e) {
    var btn = e.target.closest ? e.target.closest('[data-set-lang]') : null;
    if (!btn) return;
    var lang = btn.getAttribute('data-set-lang');
    apply(lang);
    try { localStorage.setItem(KEY, lang); } catch (err) { /* storage blocked */ }
  });
})();
