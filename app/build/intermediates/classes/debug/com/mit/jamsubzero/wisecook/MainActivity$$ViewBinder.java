// Generated code from Butter Knife. Do not modify!
package com.mit.jamsubzero.wisecook;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.mit.jamsubzero.wisecook.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492945, "field 'button' and method 'getSelectedCountries'");
    target.button = finder.castView(view, 2131492945, "field 'button'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.getSelectedCountries();
        }
      });
    view = finder.findRequiredView(source, 2131492946, "field 'button2' and method 'populateListView'");
    target.button2 = finder.castView(view, 2131492946, "field 'button2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.populateListView();
        }
      });
    view = finder.findRequiredView(source, 2131492944, "field 'listView'");
    target.listView = finder.castView(view, 2131492944, "field 'listView'");
  }

  @Override public void unbind(T target) {
    target.button = null;
    target.button2 = null;
    target.listView = null;
  }
}
